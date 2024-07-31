package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelRideRequestDto {
    private String cancel;
    private String rideId;
}
