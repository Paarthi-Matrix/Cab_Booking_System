package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideInvoiceDto {
    private int rideId;
    private String driverName;
    private String driverContactNumber;
    private String vehicleNumber;
    private String pickupPoint;
    private String dropPoint;
    private int fare;
}
