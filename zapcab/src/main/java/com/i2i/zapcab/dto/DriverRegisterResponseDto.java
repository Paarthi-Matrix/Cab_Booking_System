package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing driver registration response details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Status of the driver registration process. </li>
 *   </ol>
 * </p>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverRegisterResponseDto {
    private String status;
}
