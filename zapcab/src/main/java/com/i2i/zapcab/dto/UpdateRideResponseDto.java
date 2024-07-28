package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of an updated ride response.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Category of the vehicle assigned to the ride (e.g., sedan, SUV). </li>
 *       <li> Fare charged for the ride. </li>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *       <li> Estimated drop time for the ride. </li>
 *       <li> Actual ride time or duration of the ride. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRideResponseDto {
    private String pickupPoint;
    private String dropPoint;
    private String vehicleCategory;
    private double fare;
    private int distance;
    private String estimatedDropTime;
    private String rideTime;
}
