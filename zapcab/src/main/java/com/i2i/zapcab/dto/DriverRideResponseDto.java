package com.i2i.zapcab.dto;

import java.util.Date;


import lombok.Builder;
import lombok.Data;

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
