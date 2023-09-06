package com.abn.autoservice.parkingsystem.model;

import com.abn.autoservice.parkingsystem.constant.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Setter
@Getter
public class ParkingRequestDto implements Serializable {

    private static final long serialVersionUID = 3561427633724082472L;

    @NotNull
    @Pattern(regexp = Constants.HUMAN_NAME_REGEX)
    private String driverName;
    @NotNull
    @Pattern(regexp = Constants.LICENSE_PLATE_NO_REGEX)
    private String licensePlateNo;
    @NotNull
    @Pattern(regexp = Constants.STREET_NAME_REGEX)
    private String streetName;
    @Email
    private String emailId;
}
