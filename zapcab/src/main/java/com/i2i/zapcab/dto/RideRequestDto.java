package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of a ride request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *       <li> Category of the vehicle requested (e.g., sedan, SUV). </li>
 *       <li> Expected drop time for the ride. </li>
 *       <li> Estimated fare for the ride. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private String vehicleCategory;
    private String dropTime;
    private double fare;
}
