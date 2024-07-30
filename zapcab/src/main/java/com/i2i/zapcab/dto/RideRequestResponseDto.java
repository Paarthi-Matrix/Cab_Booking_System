package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the response details of a ride request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *       <li> Category of the vehicle assigned to the ride (e.g., sedan, SUV). </li>
 *       <li> Estimated drop time for the ride. </li>
 *       <li> Estimated fare for the ride. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestResponseDto {
    private int distance;
    private String vehicleCategory;
    private String estimatedDropTime;
    private double fare;
}