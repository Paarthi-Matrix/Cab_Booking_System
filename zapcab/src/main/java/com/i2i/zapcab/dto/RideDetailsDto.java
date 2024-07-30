package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of a ride.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the customer associated with the ride. </li>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Mobile number of the customer. </li>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideDetailsDto {
    private String customerName;
    private String pickupPoint;
    private String mobileNumber;
    private int distance;
}
