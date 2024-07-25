package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RideRequestDto {
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private String vehicleCategory;
    private String dropTime;
    private int fare;
}
