package com.abn.autoservice.parkingsystem.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Setter
@Getter
@Configuration
@ConfigurationProperties("parking")
public class ParkingSystemConfig {
    private Map<String, Integer> parkingStreetMap;
    private Map<String, Integer> paidParkingTimingMap;
    private String freeParkingDay;
}
