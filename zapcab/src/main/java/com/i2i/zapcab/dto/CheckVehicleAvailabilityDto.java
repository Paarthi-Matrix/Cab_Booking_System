package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckVehicleAvailabilityDto {
    private String pickupPoint;
    private String dropPoint;
}
