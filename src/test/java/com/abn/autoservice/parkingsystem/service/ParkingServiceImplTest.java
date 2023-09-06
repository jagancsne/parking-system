package com.abn.autoservice.parkingsystem.service;

import com.abn.autoservice.parkingsystem.config.ParkingSystemConfig;
import com.abn.autoservice.parkingsystem.entity.FineDetails;
import com.abn.autoservice.parkingsystem.entity.ParkingDetails;
import com.abn.autoservice.parkingsystem.exception.ParkingSystemException;
import com.abn.autoservice.parkingsystem.exception.RecordNotFoundException;
import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import com.abn.autoservice.parkingsystem.model.ParkingResponseDto;
import com.abn.autoservice.parkingsystem.repository.FineDetailsRepository;
import com.abn.autoservice.parkingsystem.repository.ParkingDetailsRepository;
import com.abn.autoservice.parkingsystem.util.PDFGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceImplTest {
    @InjectMocks
    private ParkingServiceImpl parkingService;
    @Mock
    private ParkingDetailsRepository parkingDetailsRepository;
    @Mock
    private FineDetailsRepository fineDetailsRepository;
    @Mock
    private ParkingSystemConfig parkingSystemConfig;
    @Mock
    private PDFGenerator pdfGenerator;

    @Test
    void testInitiateParkingWithNewRecord() {
        ParkingRequestDto requestDto = getParkingRequestDto();
        Map<String, Integer> timingMap = new HashMap<>();
        timingMap.put("startTime", 6);
        timingMap.put("endTime", 23);
        when(parkingDetailsRepository.findByLicensePlateNo(Mockito.anyString())).thenReturn(Optional.empty());
        when(parkingSystemConfig.getFreeParkingDay()).thenReturn("SUNDAY");
        when(parkingSystemConfig.getPaidParkingTimingMap()).thenReturn(timingMap);
        ParkingResponseDto responseDto = parkingService.initiateParking(requestDto);
        assertEquals("Your parking session has begun", responseDto.getMessage());
    }

    @Test
    void testInitiateParkingWithExistingRecord() {
        ParkingRequestDto requestDto = getParkingRequestDto();
        Map<String, Integer> timingMap = new HashMap<>();
        timingMap.put("startTime", 6);
        timingMap.put("endTime", 23);
        ParkingDetails parkingDetails = new ParkingDetails();
        parkingDetails.setLicensePlateNo("H-443-LD");
        when(parkingDetailsRepository.findByLicensePlateNo(Mockito.anyString())).thenReturn(Optional.of(parkingDetails));
        when(parkingSystemConfig.getFreeParkingDay()).thenReturn("SUNDAY");
        when(parkingSystemConfig.getPaidParkingTimingMap()).thenReturn(timingMap);
        ParkingResponseDto responseDto = parkingService.initiateParking(requestDto);
        assertEquals("Your parking session has begun", responseDto.getMessage());
    }

    @Test
    void testInitiateParkingWithException() {
        ParkingRequestDto requestDto = getParkingRequestDto();
        Map<String, Integer> timingMap = new HashMap<>();
        timingMap.put("startTime", 6);
        timingMap.put("endTime", 23);
        ParkingDetails parkingDetails = new ParkingDetails();
        parkingDetails.setLicensePlateNo("H-443-LD");
        when(parkingDetailsRepository.findByLicensePlateNo(Mockito.anyString())).thenThrow(
                new DataIntegrityViolationException("Invalid identifier"));
        when(parkingSystemConfig.getFreeParkingDay()).thenReturn("SUNDAY");
        when(parkingSystemConfig.getPaidParkingTimingMap()).thenReturn(timingMap);
        ParkingSystemException parkingSystemException = Assertions.assertThrows(ParkingSystemException.class, () -> parkingService.initiateParking(requestDto));
        assertEquals("Invalid identifier", parkingSystemException.getMessage());
    }

    @Test
    void testEndParking() {
        ParkingDetails parkingDetails = getParkingDetails();
        Map<String, Integer> streetMap = new HashMap<>();
        streetMap.put("Java", 10);
        streetMap.put("Spring", 13);
        when(parkingSystemConfig.getParkingStreetMap()).thenReturn(streetMap);
        when(parkingDetailsRepository.findByLicensePlateNo(Mockito.anyString())).thenReturn(Optional.of(parkingDetails));
        parkingDetails.setInTime(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        ParkingResponseDto responseDto = parkingService.endParking("H-443-LD");
        Assertions.assertEquals("Your parking session has ended", responseDto.getMessage());
    }

    @Test
    void testEndParkingWithException() {
        when(parkingDetailsRepository.findByLicensePlateNo("H-443-LD")).thenReturn(Optional.empty());
        RecordNotFoundException exception = Assertions.assertThrows(RecordNotFoundException.class, () -> parkingService.endParking("H-443-LD"));
        Assertions.assertEquals("4004", exception.getErrorCode());
    }

    @Test
    void testMonitorParking() {
        ParkingDetails parkingDetails = getParkingDetails();
        List<ParkingDetails> parkingDetailsList = List.of(parkingDetails);
        when(parkingDetailsRepository.findUnregisteredParking(false)).thenReturn(parkingDetailsList);
        Assertions.assertDoesNotThrow(() -> parkingService.monitorParking());
    }

    @Test
    void testMonitorParkingwithException() {
        ParkingDetails parkingDetails = getParkingDetails();
        List<ParkingDetails> parkingDetailsList = List.of(parkingDetails);
        when(parkingDetailsRepository.findUnregisteredParking(false)).thenReturn(parkingDetailsList);
        when(fineDetailsRepository.saveAll(Mockito.any(Iterable.class))).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertDoesNotThrow(() -> parkingService.monitorParking());
    }

    @Test
    void testGenerateReport() {
        FineDetails fineDetails = new FineDetails();
        fineDetails.setFineAmount(3.4);
        fineDetails.setLicensePlateNo("K-567-RX");
        when(fineDetailsRepository.getFineDetails(Mockito.anyString(), Mockito.eq(false), Mockito.eq(false)))
                .thenReturn(List.of(fineDetails));
        Assertions.assertDoesNotThrow(() -> parkingService.generateReport());
    }

    @Test
    void testGenerateReportWithException() {
        when(fineDetailsRepository.getFineDetails(Mockito.anyString(), Mockito.eq(false), Mockito.eq(false)))
                .thenThrow(new DataIntegrityViolationException("Data type mismatch"));
        Assertions.assertDoesNotThrow(() -> parkingService.generateReport());
    }

    private static ParkingRequestDto getParkingRequestDto() {
        ParkingRequestDto requestDto = new ParkingRequestDto();
        requestDto.setDriverName("test");
        requestDto.setLicensePlateNo("H-443-LD");
        requestDto.setStreetName("Spring");
        return requestDto;
    }

    private static ParkingDetails getParkingDetails() {
        ParkingDetails parkingDetails = new ParkingDetails();
        parkingDetails.setLicensePlateNo("H-443-LD");
        parkingDetails.setRegistered(true);
        parkingDetails.setParkingStreetName("Java");
        return parkingDetails;
    }

}
