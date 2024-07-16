package com.i2i.zapcab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.PASSWORD_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.PASSWORD_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.PASSWORD_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.PASSWORD_SIZE;

@Builder
@Data
public class UpdateDriverPasswordDto {
    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String contactNumber;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Size(min = 6, message = PASSWORD_SIZE)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_PATTERN_MESSAGE)
    private String password;
}
