package com.abn.autoservice.parkingsystem.handler;

import com.abn.autoservice.parkingsystem.constant.Constants;
import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import com.abn.autoservice.parkingsystem.exception.ParkingSystemException;
import com.abn.autoservice.parkingsystem.exception.RecordNotFoundException;
import com.abn.autoservice.parkingsystem.model.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ParkingSystemExceptionHandlerTest {

    private final ParkingSystemExceptionHandler exceptionHandler = new ParkingSystemExceptionHandler();

    @Test
    void testHandleInvalidInputException() {
        InvalidInputException exception = new InvalidInputException("Invalid email", Constants.INVALID_INPUT_CODE);
        ResponseEntity<ErrorResponseDto> errorResponseEntity = exceptionHandler.handleInvalidInputException(exception);
        assertEquals("4550", errorResponseEntity.getBody().getErrorCode());
    }

    @Test
    void testHandleRecordNotFoundException() {
        RecordNotFoundException exception = new RecordNotFoundException("Data not found", Constants.NOT_FOUND_CODE);
        ResponseEntity<ErrorResponseDto> errorResponseEntity = exceptionHandler.handleRecordNotFoundException(exception);
        assertEquals("4004", errorResponseEntity.getBody().getErrorCode());
    }

    @Test
    void testHandleParkingSystemException() {
        ParkingSystemException exception = new ParkingSystemException("System error");
        ResponseEntity<ErrorResponseDto> errorResponseEntity = exceptionHandler.handleParkingSystemException(exception);
        assertEquals("5000", errorResponseEntity.getBody().getErrorCode());
    }

    @Test
    void testHandleOtherException() {
        NumberFormatException exception = new NumberFormatException("Invalid format");
        ResponseEntity<ErrorResponseDto> errorResponseEntity = exceptionHandler.handleOtherException(exception);
        assertEquals("5000", errorResponseEntity.getBody().getErrorCode());
    }
}
