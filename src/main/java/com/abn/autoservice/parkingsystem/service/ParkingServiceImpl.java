package com.abn.autoservice.parkingsystem.service;

import com.abn.autoservice.parkingsystem.config.ParkingSystemConfig;
import com.abn.autoservice.parkingsystem.constant.Constants;
import com.abn.autoservice.parkingsystem.entity.FineDetails;
import com.abn.autoservice.parkingsystem.entity.ParkingDetails;
import com.abn.autoservice.parkingsystem.exception.ParkingSystemException;
import com.abn.autoservice.parkingsystem.exception.RecordNotFoundException;
import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import com.abn.autoservice.parkingsystem.model.ParkingResponseDto;
import com.abn.autoservice.parkingsystem.repository.FineDetailsRepository;
import com.abn.autoservice.parkingsystem.repository.ParkingDetailsRepository;
import com.abn.autoservice.parkingsystem.util.PDFGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.abn.autoservice.parkingsystem.constant.Constants.NOT_FOUND_CODE;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingServiceImpl.class);
    private final ParkingDetailsRepository parkingDetailsRepository;
    private final FineDetailsRepository fineDetailsRepository;
    private final ParkingSystemConfig parkingSystemConfig;
    private final PDFGenerator pdfGenerator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ParkingResponseDto initiateParking(ParkingRequestDto parkingRequestDto) {
        logger.debug("Enter - initiateParking method");
        LocalDateTime currentTimestamp = LocalDateTime.now();
        try {
            boolean registered = isRegistered(currentTimestamp);
            Optional<ParkingDetails> parkingDetailsOptional = parkingDetailsRepository.findByLicensePlateNo(parkingRequestDto.getLicensePlateNo());
            ParkingDetails parkingDetails = new ParkingDetails();
            if (parkingDetailsOptional.isPresent()) {
                parkingDetails.setId(parkingDetailsOptional.get().getId());
                parkingDetails.setOutTime(null);
            }
            parkingDetails.setDriverName(parkingRequestDto.getDriverName());
            parkingDetails.setEmail(parkingRequestDto.getEmailId());
            parkingDetails.setRegistered(registered);
            parkingDetails.setParkingStreetName(parkingRequestDto.getStreetName());
            parkingDetails.setLicensePlateNo(parkingRequestDto.getLicensePlateNo());
            parkingDetails.setInTime(Timestamp.valueOf(currentTimestamp));
            parkingDetailsRepository.save(parkingDetails);
        } catch (RuntimeException e) {
            logger.error("Error while updating parking details {}", e.getMessage(), e);
            throw new ParkingSystemException(e.getMessage(), e);
        }
        logger.info("Parking details with {} is saved successfully", parkingRequestDto.getLicensePlateNo());
        ParkingResponseDto responseDto = new ParkingResponseDto();
        responseDto.setMessage("Your parking session has begun");
        responseDto.setStartedAt(currentTimestamp);
        logger.debug("Exit - initiateParking method");
        return responseDto;
    }

    private boolean isRegistered(LocalDateTime currentTimestamp) {
        String dayOfWeek = currentTimestamp.getDayOfWeek().name();
        String freeParkingDay = parkingSystemConfig.getFreeParkingDay();
        boolean registered = false;
        if (!dayOfWeek.equalsIgnoreCase(freeParkingDay)) {
            Integer startHourOfday = parkingSystemConfig.getPaidParkingTimingMap().get("startTime");
            Integer endHourOfday = parkingSystemConfig.getPaidParkingTimingMap().get("endTime");
            if (currentTimestamp.getHour() >= startHourOfday && currentTimestamp.getHour() <= endHourOfday) {
                registered = true;
            }
        }
        return registered;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ParkingResponseDto endParking(String licensePlateNo) {
        logger.debug("Enter - endParking method");
        Optional<ParkingDetails> parkingInfoOptional = parkingDetailsRepository.findByLicensePlateNo(licensePlateNo);
        if (parkingInfoOptional.isEmpty()) {
            throw new RecordNotFoundException("Parking info is not found for given license plate number", NOT_FOUND_CODE);
        }
        LocalDateTime currentTimestamp = LocalDateTime.now();
        ParkingDetails parkingDetails = parkingInfoOptional.get();
        Timestamp inTime = parkingDetails.getInTime();
        if (parkingDetails.isRegistered()) {
            int parkingMinutes = (int) Duration.between(inTime.toLocalDateTime(), currentTimestamp).toMinutes();
            Integer centPerMin = parkingSystemConfig.getParkingStreetMap().get(parkingDetails.getParkingStreetName());
            double parkingCharge = (double) parkingMinutes * centPerMin / 100;
            parkingDetails.setParkingCharge(parkingCharge);
        }
        parkingDetails.setOutTime(Timestamp.valueOf(currentTimestamp));
        parkingDetailsRepository.save(parkingDetails);
        ParkingResponseDto responseDto = new ParkingResponseDto();
        responseDto.setMessage("Your parking session has ended");
        responseDto.setOwes(parkingDetails.getParkingCharge() + " euros");
        logger.debug("Exit - endParking method");
        return responseDto;
    }

    @Override
    @Scheduled(cron = Constants.MONITOR_SCHEDULER_CRON_EX)
    public void monitorParking() {
        logger.info("Executing monitorParking scheduler method at {}", LocalDateTime.now());
        try {
            List<ParkingDetails> unregisteredParkingList = parkingDetailsRepository.findUnregisteredParking(false);
            if (!unregisteredParkingList.isEmpty()) {
                LocalDateTime currentTimestamp = LocalDateTime.now();
                List<FineDetails> fineDetailList = new ArrayList<>();
                for (ParkingDetails parkingInfo : unregisteredParkingList) {
                    FineDetails fineDetails = new FineDetails();
                    fineDetails.setDriverName(parkingInfo.getDriverName());
                    fineDetails.setLicensePlateNo(parkingInfo.getLicensePlateNo());
                    fineDetails.setStreetName(parkingInfo.getParkingStreetName());
                    fineDetails.setEmail(parkingInfo.getEmail());
                    fineDetails.setObservationDate(Timestamp.valueOf(currentTimestamp));
                    fineDetails.setFineAmount(10.0);
                    fineDetailList.add(fineDetails);
                }
                fineDetailsRepository.saveAll(fineDetailList);
            }
        } catch (NestedRuntimeException e) {
            logger.error("Error while executing monitorParking scheduler", e);
        }
    }

    @Override
    @Scheduled(cron = Constants.REPORTING_SCHEDULER_CRON_EX)
    public void generateReport() {
        logger.info("Executing generateReport scheduler method at {}", LocalDateTime.now());
        List<FineDetails> fineDetailsList = new ArrayList<>();
        try {
            fineDetailsList = fineDetailsRepository.getFineDetails(LocalDate.now().toString(),
                    false, false);
        } catch (DataAccessException e) {
            logger.error("Error while reading fine details from database", e);
        }
        for (FineDetails fineInfo : fineDetailsList) {
            pdfGenerator.generatePdfReport(fineInfo);
        }
    }
}
