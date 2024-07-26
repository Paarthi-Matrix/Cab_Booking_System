package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponseDto {
    private String pickupPoint;
    private String dropPoint;
    private String vehicleCategory;
    private double fare;
    private int distance;
    private String estimatedDropTime;
    private String rideTime;
}
