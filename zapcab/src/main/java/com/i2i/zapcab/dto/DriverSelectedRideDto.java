package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing the details of a ride selected by a driver.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Mobile number of the driver. </li>
 *       <li> ID of the selected ride. </li>
 *       <li> Name of the customer who requested the ride. </li>
 *   </ol>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverSelectedRideDto {
    private String mobileNumber;
    private String rideId;
    private String customerName;
}
