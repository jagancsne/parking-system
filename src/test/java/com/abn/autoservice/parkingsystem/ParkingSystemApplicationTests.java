package com.abn.autoservice.parkingsystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParkingSystemApplicationTests {
	@Autowired
	private ParkingSystemApplication parkingSystemApplication;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(parkingSystemApplication);
	}

}
