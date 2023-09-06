package com.abn.autoservice.parkingsystem.validator;

import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InputValidatorTest {
    private final InputValidator inputValidator = new InputValidator();


    @Test
    void testValidateRequestParams() {
        ParkingRequestDto parkingRequestDto = new ParkingRequestDto();
        parkingRequestDto.setStreetName("test");
        parkingRequestDto.setDriverName("test");
        parkingRequestDto.setLicensePlateNo("X-123-XD");
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class,
                () -> inputValidator.validateRequestParams(parkingRequestDto));
        Assertions.assertEquals("4550", exception.getErrorCode());
    }
}
