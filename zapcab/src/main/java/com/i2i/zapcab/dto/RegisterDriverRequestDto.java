package com.i2i.zapcab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.i2i.zapcab.constant.ZapCabConstant.DOB_NOT_NULL;
import static com.i2i.zapcab.constant.ZapCabConstant.DOB_PAST_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.EMAIL_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.EMAIL_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.EMAIL_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.GENDER_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.GENDER_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.LICENSE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.LICENSE_NUMBER_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.LICENSE_PLATE_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.LICENSE_PLATE_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.MOBILE_NUMBER_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.NAME_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.NAME_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.RC_BOOK_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.REGION_NOT_BLANK;
import static com.i2i.zapcab.constant.ZapCabConstant.REGION_PATTERN_MESSAGE;
import static com.i2i.zapcab.constant.ZapCabConstant.STRING_REGEX;
import static com.i2i.zapcab.constant.ZapCabConstant.VALID_GENDER;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDriverRequestDto {
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = NAME_PATTERN_MESSAGE)
    private String name;

    @NotBlank(message = REGION_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = REGION_PATTERN_MESSAGE)
    private String region;
    @NotBlank(message = REGION_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = REGION_PATTERN_MESSAGE)
    private String city;

    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String mobileNumber;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_PATTERN_MESSAGE)
    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_PATTERN_MESSAGE)
    private String email;

    @NotBlank(message = LICENSE_NUMBER_NOT_BLANK)
    @Pattern(regexp = LICENSE_NUMBER_REGEX)
    private String licenseNumber;

    @NotBlank(message = RC_BOOK_NOT_BLANK)
    private String rcBookNo;

    @NotNull(message = DOB_NOT_NULL)
    @Past(message = DOB_PAST_MESSAGE)
    private LocalDate dateOfBirth;

    @NotBlank(message = GENDER_NOT_BLANK)
    @Pattern(regexp = VALID_GENDER, message = GENDER_PATTERN_MESSAGE)
    private String gender;

    @NotBlank(message = LICENSE_PLATE_NOT_BLANK)
    @Pattern(regexp = LICENSE_PLATE_REGEX)
    private String licensePlate;
    private String category;
    private String type;
    private String model;
}

