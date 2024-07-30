package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_REGEX;
import static com.i2i.zapcab.common.ZapCabConstant.NAME_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.NAME_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.STRING_REGEX;

/**
 * <p>
 * This class is responsible for managing the details of updating a pending request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Phone number associated with the pending request. </li>
 *       <li> Status to be updated for the pending request. </li>
 *       <li> Additional remarks or comments about the pending request. </li>
 *   </ol>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdatePendingRequestDto {
    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String mobileNumber;
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = NAME_PATTERN_MESSAGE)
    private String status;
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = NAME_PATTERN_MESSAGE)
    private String remarks;
}