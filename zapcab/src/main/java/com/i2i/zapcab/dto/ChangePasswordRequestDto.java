package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing change password request details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> New password to be set for the user. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    String newPassword;
}
