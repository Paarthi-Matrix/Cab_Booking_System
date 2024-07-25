package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAvailabilityResponseDto {
    private String pickup;
    private String drop;
    private List<RideRequestResponseDto> rideRequestResponseDtos;
}
