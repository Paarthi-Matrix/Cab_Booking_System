package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverSelectedRideDto {
    private String mobileNumber;
    private int rideId;
    private String customerName;
}
