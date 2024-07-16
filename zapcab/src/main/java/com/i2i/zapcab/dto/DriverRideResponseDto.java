package com.i2i.zapcab.dto;

import java.util.Date;


import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.i2i.zapcab.constant.ZapCabConstant.STRING_REGEX;

@Builder
@Data
public class DriverRideResponseDto {
    private String customerName;
    private Long customerContactNumber;
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private Date dropTime;
}
