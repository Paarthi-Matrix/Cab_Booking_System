package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

/**
 * This lass represents the data transfer object
 * which shows the vehicle availability categorizing throught hte pickup point
 */
@Builder
@Data
public class CheckVehicleAvailabilityDto {
    private String pickupPoint;
    private String dropPoint;
}
