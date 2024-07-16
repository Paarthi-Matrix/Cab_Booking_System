package com.i2i.zapcab.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.i2i.zapcab.constant.ZapCabConstant.STRING_REGEX;

@Builder
@Data
public class CheckVehicleAvailabilityDto {
    private String pickupPoint;
    private String dropPoint;
}
