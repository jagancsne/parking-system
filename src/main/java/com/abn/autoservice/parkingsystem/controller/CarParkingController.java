package com.abn.autoservice.parkingsystem.controller;

import com.abn.autoservice.parkingsystem.constant.Constants;
import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import com.abn.autoservice.parkingsystem.model.ParkingResponseDto;
import com.abn.autoservice.parkingsystem.service.ParkingService;
import com.abn.autoservice.parkingsystem.validator.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.abn.autoservice.parkingsystem.constant.Constants.INVALID_INPUT_CODE;

@RestController
@RequestMapping("/car-parking")
public class CarParkingController {
    private static final Logger logger = LoggerFactory.getLogger(CarParkingController.class);
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private InputValidator inputValidator;

    @PostMapping(value = "/start", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingResponseDto> startParking(@RequestBody ParkingRequestDto parkingRequestDto) {
        logger.debug("Enter - startParking method");
        inputValidator.validateRequestParams(parkingRequestDto);
        ParkingResponseDto responseDto = parkingService.initiateParking(parkingRequestDto);
        logger.debug("Exit - startParking method");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping(value = "/end", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingResponseDto> endParking(@RequestParam("licensePlateNo") String licensePlateNo) {
        logger.debug("Enter - endParking method");
        if (!licensePlateNo.matches(Constants.LICENSE_PLATE_NO_REGEX)) {
            throw new InvalidInputException(String.format("'%s' is not valid. please enter the correct value", licensePlateNo), INVALID_INPUT_CODE);
        }
        ParkingResponseDto responseDto = parkingService.endParking(licensePlateNo);
        logger.debug("Exit - endParking method");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }

}
