package com.i2i.zapcab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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
 * <p>
 * This class is responsible for managing the details required to register a new customer.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the customer. </li>
 *       <li> Mobile number of the customer. </li>
 *       <li> Email address of the customer. </li>
 *       <li> Gender of the customer. </li>
 *       <li> Password for the customer's account. </li>
 *       <li> Date of birth of the customer. </li>
 *   </ol>
 * </p>
 * <p>
 * <b>Validation Constraints:</b>
 *   <ul>
 *       <li> Name: Must not be blank and should match the specified pattern. </li>
 *       <li> Mobile Number: Must not be blank and should match the specified pattern. </li>
 *       <li> Email: Must not be blank, must be a valid email format, and should match the specified pattern. </li>
 *       <li> Gender: Must not be blank and should match the specified pattern. </li>
 *       <li> Password: Must not be blank, should have a minimum length of 6 characters, and match the specified pattern. </li>
 *       <li> Date of Birth: Must not be null and should be a past date. </li>
 *   </ul>
 * </p>
 */
@Builder
@Data
public class RegisterCustomerDto {
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
}

