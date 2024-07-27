package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of an updated ride request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Category of the vehicle requested for the ride (e.g., sedan, SUV). </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRideDto {
    private String pickupPoint;
    private String dropPoint;
    private String vehicleCategory;
}
