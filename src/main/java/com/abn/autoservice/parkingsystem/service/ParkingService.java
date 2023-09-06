package com.abn.autoservice.parkingsystem.service;

import com.abn.autoservice.parkingsystem.model.ParkingRequestDto;
import com.abn.autoservice.parkingsystem.model.ParkingResponseDto;

public interface ParkingService {
    ParkingResponseDto initiateParking(ParkingRequestDto parkingRequestDto);
    ParkingResponseDto endParking(String licensePlateNo);
    void monitorParking();
    void generateReport();
}
