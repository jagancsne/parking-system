package com.abn.autoservice.parkingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ParkingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingSystemApplication.class, args);
	}

}
