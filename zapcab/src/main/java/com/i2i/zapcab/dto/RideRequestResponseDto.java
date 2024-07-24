package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data transfer object for providing the token to the authorised
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestResponseDto {
    private int distance;
    private String vehicleCategory;
    private String estimatedDropTime;
    private double fare;
}