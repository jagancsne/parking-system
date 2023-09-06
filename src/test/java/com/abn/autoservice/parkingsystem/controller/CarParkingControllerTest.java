package com.abn.autoservice.parkingsystem.controller;

import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import com.abn.autoservice.parkingsystem.model.ParkingResponseDto;
import com.abn.autoservice.parkingsystem.service.ParkingService;
import com.abn.autoservice.parkingsystem.validator.InputValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CarParkingControllerTest {

    @InjectMocks
    private CarParkingController carParkingController;
    @Mock
    private ParkingService parkingService;
    @Mock
    private InputValidator inputValidator;

    @Test
    void testStartParking() {
        ParkingRequestDto requestDto = new ParkingRequestDto();
        requestDto.setLicensePlateNo("DD-477-FX");
        requestDto.setDriverName("test");
        ParkingResponseDto responseDto = new ParkingResponseDto();
        responseDto.setMessage("test");
        Mockito.doNothing().when(inputValidator).validateRequestParams(Mockito.any(ParkingRequestDto.class));
        Mockito.when(parkingService.initiateParking(requestDto)).thenReturn(responseDto);
        ResponseEntity<ParkingResponseDto> responseEntity = carParkingController.startParking(requestDto);
        ParkingResponseDto parkingResponseDto = responseEntity.getBody();
        Assertions.assertEquals("test", parkingResponseDto.getMessage());
    }

    @Test
    void testEndParking() {
        ParkingResponseDto responseDto = new ParkingResponseDto();
        responseDto.setMessage("test");
        Mockito.when(parkingService.endParking(Mockito.anyString())).thenReturn(responseDto);
        ResponseEntity<ParkingResponseDto> responseEntity = carParkingController.endParking("D-673-FD");
        ParkingResponseDto parkingResponseDto = responseEntity.getBody();
        Assertions.assertEquals("test", parkingResponseDto.getMessage());
    }

    @Test
    void testEndParkingWithInvalidInput() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () ->
                carParkingController.endParking("D-6731-FD"));
        Assertions.assertTrue(exception.getMessage().contains("D-6731-FD"));
    }

}
