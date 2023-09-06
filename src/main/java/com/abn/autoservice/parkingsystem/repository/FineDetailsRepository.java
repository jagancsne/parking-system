package com.abn.autoservice.parkingsystem.repository;

import com.abn.autoservice.parkingsystem.entity.FineDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FineDetailsRepository extends JpaRepository<FineDetails, Long> {
    @Query(value = "SELECT * FROM fine_details WHERE FORMATDATETIME(observation_date ,'yyyy-MM-dd') = :date" +
            " AND fine_paid = :paid AND notified = :notified ", nativeQuery = true)
    List<FineDetails> getFineDetails(@Param("date") String date, @Param("paid") Boolean paid, @Param("notified") Boolean notified);
}
