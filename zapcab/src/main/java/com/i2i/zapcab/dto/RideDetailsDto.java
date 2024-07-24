package com.i2i.zapcab.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideDetailsDto {
    private String customerName;
    private String pickupPoint;
    private String mobileNumber;
    private int distance;

}
