package com.abn.autoservice.parkingsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
@Setter
@Getter
public class ErrorResponseDto implements Serializable {
    private static final long serialVersionUID = 286986000901092529L;
    private String errorCode;
    private String errorType;
    private String errorMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp;
}
