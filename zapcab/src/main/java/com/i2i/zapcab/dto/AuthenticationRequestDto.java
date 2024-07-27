package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing authentication request details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Phone number of the user attempting to authenticate. </li>
 *       <li> Password of the user attempting to authenticate. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    private String phoneNumber;
    private String password;
}
