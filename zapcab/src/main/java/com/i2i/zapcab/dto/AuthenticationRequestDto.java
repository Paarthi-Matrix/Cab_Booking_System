package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_REGEX;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_NOT_BLANK;

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
    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String mobileNumber;
    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;
}