package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing the details of a ride invoice.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Unique identifier for the ride. </li>
 *       <li> Name of the driver for the ride. </li>
 *       <li> Contact number of the driver. </li>
 *       <li> Vehicle number used for the ride. </li>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Fare charged for the ride. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideInvoiceDto {
    private String rideId;
    private String driverName;
    private String driverContactNumber;
    private String vehicleNumber;
    private String pickupPoint;
    private String dropPoint;
    private double fare;
}
