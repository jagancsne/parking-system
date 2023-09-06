package com.abn.autoservice.parkingsystem.handler;

import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import com.abn.autoservice.parkingsystem.exception.ParkingSystemException;
import com.abn.autoservice.parkingsystem.exception.RecordNotFoundException;
import com.abn.autoservice.parkingsystem.model.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.abn.autoservice.parkingsystem.constant.Constants.*;

@ControllerAdvice
public class ParkingSystemExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInputException(InvalidInputException exception){
        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(exception.getErrorCode());
        response.setErrorMessage(exception.getErrorMessage());
        response.setErrorType(INVALID_REQUEST);
        response.setTimestamp(Timestamp.valueOf(exception.getDateTime()));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRecordNotFoundException(RecordNotFoundException exception){
        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(exception.getErrorCode());
        response.setErrorMessage(exception.getErrorMessage());
        response.setErrorType(NOT_FOUND);
        response.setTimestamp(Timestamp.valueOf(exception.getDateTime()));
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ParkingSystemException.class)
    public ResponseEntity<ErrorResponseDto> handleParkingSystemException(ParkingSystemException exception){
        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(SYSTEM_ERROR_CODE);
        response.setErrorMessage(exception.getErrorMessage());
        response.setErrorType(SYSTEM_ERROR);
        response.setTimestamp(Timestamp.valueOf(exception.getDateTime()));
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOtherException(Exception exception){
        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(SYSTEM_ERROR_CODE);
        response.setErrorMessage("System error");
        response.setErrorType(SYSTEM_ERROR);
        response.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(response, status);
    }
}
