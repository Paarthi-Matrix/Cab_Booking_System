package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of available drivers.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the available driver. </li>
 *       <li> Mobile number of the available driver. </li>
 *       <li> License plate of the vehicle driven by the available driver. </li>
 *       <li> Current location of the available driver. </li>
 *       <li> Category of the vehicle (e.g., sedan, SUV). </li>
 *       <li> Type of the vehicle (e.g., electric, hybrid). </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDriverDto {
    private String name;
    private String mobileNumber;
    private String licensePlate;
    private String location;
    private String category;
    private String type;
}
