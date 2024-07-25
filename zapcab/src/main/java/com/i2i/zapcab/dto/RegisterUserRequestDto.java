package com.i2i.zapcab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.i2i.zapcab.helper.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static com.i2i.zapcab.common.ZapCabConstant.DOB_NOT_NULL;
import static com.i2i.zapcab.common.ZapCabConstant.DOB_PAST_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.EMAIL_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.EMAIL_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.EMAIL_REGEX;
import static com.i2i.zapcab.common.ZapCabConstant.GENDER_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.GENDER_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.MOBILE_NUMBER_REGEX;
import static com.i2i.zapcab.common.ZapCabConstant.NAME_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.NAME_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_NOT_BLANK;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_PATTERN_MESSAGE;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_REGEX;
import static com.i2i.zapcab.common.ZapCabConstant.PASSWORD_SIZE;
import static com.i2i.zapcab.common.ZapCabConstant.STRING_REGEX;

/**
 * <p> Data Transfer Object for registering a user.
 * This class contains the necessary fields required for user registration.
 *  The required fields are  :
 *     Name : Must be in Alphabets.
 *     Email : A valid email address which must have atleast @ and .com.
 *     Mobile number : A 10 digit valid mobile number.
 *     Password : A strong password must be created which must hold Atleast 1 capital letter, 1 special character
 *     and 1 number.
 *     Role : A role must be specified either Admin, customer or driver.
 *     Date of birth : This field shoul be in a pattern yyyy-MM-dd.
 *     </p>
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserRequestDto {

    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message =NAME_PATTERN_MESSAGE)
    private String name;

    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String mobileNumber;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_PATTERN_MESSAGE)
    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_PATTERN_MESSAGE)
    private String email;

    @NotBlank(message = GENDER_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = GENDER_PATTERN_MESSAGE)
    private String gender;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Size(min = 6, message = PASSWORD_SIZE)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_PATTERN_MESSAGE)
    private String password;

    @NotNull(message = DOB_NOT_NULL)
    @Past(message = DOB_PAST_MESSAGE)
    private LocalDate dateOfBirth;

    private List<RoleEnum> role;
}
