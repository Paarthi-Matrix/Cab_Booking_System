package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the response of an OTP (One-Time Password) request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> A message indicating the result or status of the OTP request. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPResponseDto {
    String msg;
}
