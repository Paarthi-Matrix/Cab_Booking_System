package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the request to mask or unmask a mobile number.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> A flag indicating whether the mobile number should be masked or not. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskMobileNumberRequestDto {
    Boolean isMaskedMobileNumber;
}
