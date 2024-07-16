package com.i2i.zapcab.dto;

import java.util.Date;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.i2i.zapcab.constant.ZapCabConstant.STRING_REGEX;

@Builder
@Data
public class RideRequestDto {
    private String pickupPoint;
    private String dropPoint;
    @Pattern(regexp = STRING_REGEX)
    private String vehicleCategory;
    private int distance;
    private int fare;
    private Date dropTime;
}
