package com.abn.autoservice.parkingsystem.exception;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class ParkingSystemException extends RuntimeException {
    private final String errorMessage;
    private final LocalDateTime dateTime = LocalDateTime.now();

    public ParkingSystemException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ParkingSystemException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
    }
}
