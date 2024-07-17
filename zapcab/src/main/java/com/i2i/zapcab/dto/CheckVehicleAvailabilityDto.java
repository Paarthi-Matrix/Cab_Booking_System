package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckVehicleAvailabilityDto {
    private String pickupPoint;
    private String dropPoint;
}
