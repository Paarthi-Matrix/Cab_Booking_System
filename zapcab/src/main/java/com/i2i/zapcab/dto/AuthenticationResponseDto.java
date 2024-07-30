package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.NAME_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.NAME_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.STRING_REGEX;

/**
 * <p>
 * This class is responsible for managing authentication response details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Authentication token generated upon successful login. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = NAME_PATTERN_MESSAGE)
    private String token;
}
