package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * This class is responsible for managing the details of a requested ride.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Unique identifier for the ride request. </li>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *       <li> Name of the customer who requested the ride. </li>
 *       <li> Mobile number of the customer who requested the ride. </li>
 *   </ol>
 * </p>
 */
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
