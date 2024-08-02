package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.*;

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
@NoArgsConstructor
@AllArgsConstructor
public class CheckVehicleAvailabilityDto {
    @NotBlank(message = PICKUP_POINT_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = PICKUP_POINT_PATTERN_MESSAGE)
    private String pickupPoint;

    @NotBlank(message = DROP_POINT_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = DROP_POINT_PATTERN_MESSAGE)
    private String dropPoint;
}
