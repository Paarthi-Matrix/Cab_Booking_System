package com.i2i.zapcab.dto;

import lombok.*;

/**
 * <p>
 * This class is responsible for managing email request details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the driver associated with the email request. </li>
 *       <li> Mobile number of the driver. </li>
 *       <li> Password related to the email request. </li>
 *       <li> Recipient email address. </li>
 *   </ol>
 * </p>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailRequestDto {
    String UserName;
    String mobilNumber;
    String password;
    String toEmail;
}
