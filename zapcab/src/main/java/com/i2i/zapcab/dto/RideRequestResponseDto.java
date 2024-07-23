package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestResponseDto {
    private int distance;
    private String vehicleCategory;
    private String estimatedDropTime;
    private double fare;
}
