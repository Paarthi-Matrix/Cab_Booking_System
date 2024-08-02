package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of an assigned driver.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> One-time password (OTP) for driver assignment. </li>
 *       <li> Name of the assigned driver. </li>
 *       <li> Mobile number of the assigned driver. </li>
 *       <li> Driver's ratings. </li>
 *       <li> Category of the vehicle assigned to the driver (e.g., sedan, SUV). </li>
 *       <li> Model of the vehicle assigned to the driver. </li>
 *       <li> License plate of the vehicle assigned to the driver. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedDriverDto {
    private String Otp;
    private String name;
    private String mobileNumber;
    private double ratings;
    private String category;
    private String model;
    private String licensePlate;
}
