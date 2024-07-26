package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestedRideDto {
    private String rideId;
    private String pickUpPoint;
    private String dropPoint;
    private int distance;
    private String customerName;
    private String mobileNumber;
}
