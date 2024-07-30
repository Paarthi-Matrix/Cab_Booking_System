package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.*;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_PATTERN_MESSAGE;

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
    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Size(min = 6, message = PASSWORD_SIZE)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_PATTERN_MESSAGE)
    String newPassword;
}
