package com.i2i.zapcab.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RideRequestDto {
    private String pickupPoint;
    private String dropPoint;
    private String vehicleCategory;
    private int distance;
    private int fare;
    private Date dropTime;
}
