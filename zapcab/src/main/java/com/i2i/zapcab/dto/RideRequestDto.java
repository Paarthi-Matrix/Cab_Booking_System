package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class RideRequestDto {
    private String pickupPoint;
    private String dropPoint;
    private String vehicleCategory;
    private int fare;
    private Date dropTime;
    private int distance;
}
