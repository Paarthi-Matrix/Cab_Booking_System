package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class DriverRideResponseDto {
    private String customerName;
    private Long customerContactNumber;
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private Date dropTime;
}
