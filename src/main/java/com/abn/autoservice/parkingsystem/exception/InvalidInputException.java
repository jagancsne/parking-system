package com.abn.autoservice.parkingsystem.exception;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class InvalidInputException extends RuntimeException {
    private final String errorMessage;
    private final String errorCode;
    private final LocalDateTime dateTime = LocalDateTime.now();

    public InvalidInputException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public InvalidInputException(String errorMessage, String errorCode, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
