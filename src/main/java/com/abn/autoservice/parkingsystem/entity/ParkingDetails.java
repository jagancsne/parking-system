package com.abn.autoservice.parkingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "parking_details")
public class ParkingDetails implements Serializable {

    private static final long serialVersionUID = -5038571497714065517L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "in_time")
    private Timestamp inTime;
    @Column(name = "out_time")
    private Timestamp outTime;
    @Column(name = "parking_street_name")
    private String parkingStreetName;
    @Column(name = "parking_charge")
    private Double parkingCharge;
    @Column(name = "license_plate_no")
    private String licensePlateNo;
    @Column(name = "is_registered")
    private boolean registered;
    @Column(name = "driver_name", nullable = false)
    private String driverName;
    @Column(name = "email")
    private String email;

}
