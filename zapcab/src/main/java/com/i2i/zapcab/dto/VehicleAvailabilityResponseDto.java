package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * This class is responsible for managing the details of vehicle availability responses.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> List of ride request responses that are available based on the vehicle's availability. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAvailabilityResponseDto {
    private String pickup;
    private String drop;
    private List<RideRequestResponseDto> rideRequestResponseDtos;
}
