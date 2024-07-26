package com.i2i.zapcab.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RideResponseDto {
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private double fare;
    private Date dropTime;
    private String driverName;
    private Long driverContactNumber;
    private String vehicleNumber;
    private int rating;
}
