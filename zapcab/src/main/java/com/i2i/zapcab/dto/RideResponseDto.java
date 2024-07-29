package com.i2i.zapcab.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * This class is responsible for managing the details of a ride response.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Distance of the ride in kilometers or miles. </li>
 *       <li> Fare charged for the ride. </li>
 *       <li> Drop time of the ride. </li>
 *       <li> Name of the driver who completed the ride. </li>
 *       <li> Contact number of the driver. </li>
 *       <li> Vehicle number used for the ride. </li>
 *       <li> Rating given for the ride, typically on a scale (e.g., 1 to 5). </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
public class RideResponseDto {
    private String pickupPoint;
    private String dropPoint;
    private int distance;
    private double fare;
    private LocalDateTime dropTime;
    private String driverName;
    private Long driverContactNumber;
    private String vehicleNumber;
    private int rating;
}
