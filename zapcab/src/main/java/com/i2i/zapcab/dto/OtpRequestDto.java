package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of an OTP (One-Time Password) request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Mobile number of the customer requesting the OTP. </li>
 *       <li> The OTP code sent to the customer. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequestDto {
    private String customerMobileNumber;
    private String otp;
}
