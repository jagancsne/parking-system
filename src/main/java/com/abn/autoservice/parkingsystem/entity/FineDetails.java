package com.abn.autoservice.parkingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "fine_details")
public class FineDetails implements Serializable {
    private static final long serialVersionUID = 9119923298706821800L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "driver_name", nullable = false)
    private String driverName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "fine_amount")
    private Double fineAmount;
    @Column(name = "notified")
    private boolean notified;
    @Column(name = "fine_paid")
    private boolean finePaid;
    @Column(name = "license_plate_no")
    private String licensePlateNo;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "observation_date")
    private Timestamp observationDate;
}
