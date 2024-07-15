package com.i2i.zapcab.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class RideResponseDto {
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private int fare;
    private Date dropTime;
    private String driverName;
    private Long driverContactNumber;
    private String vehicleNumber;
    private int rating;
}
