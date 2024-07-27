package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of a driver status update request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Status of the driver (e.g., available, busy). </li>
 *       <li> Current location of the driver. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDriverStatusDto {
    String status;
    String location;
}
