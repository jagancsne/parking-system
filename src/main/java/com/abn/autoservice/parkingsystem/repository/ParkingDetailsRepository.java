package com.abn.autoservice.parkingsystem.repository;

import com.abn.autoservice.parkingsystem.entity.ParkingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingDetailsRepository extends JpaRepository<ParkingDetails, Long> {
    Optional<ParkingDetails> findByLicensePlateNo(String licensePlateNo);

    @Query(nativeQuery = true, value = "select * from parking_details where in_time IS NOT NULL and out_time IS NULL and is_registered = :registered")
    List<ParkingDetails> findUnregisteredParking(@Param("registered") Boolean registered);
}
