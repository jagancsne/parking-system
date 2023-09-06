package com.abn.autoservice.parkingsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class ParkingResponseDto implements Serializable {

    private static final long serialVersionUID = -2256318547421913893L;
    private String message;
    private String owes;
    private LocalDateTime startedAt;
}
