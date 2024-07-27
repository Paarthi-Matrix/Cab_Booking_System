package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * This class is responsible for managing vehicle availability check request details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Pickup point location. </li>
 *       <li> Drop point location. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
public class CheckVehicleAvailabilityDto {
    private String pickupPoint;
    private String dropPoint;
}
