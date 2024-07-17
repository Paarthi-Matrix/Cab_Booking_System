package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

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
