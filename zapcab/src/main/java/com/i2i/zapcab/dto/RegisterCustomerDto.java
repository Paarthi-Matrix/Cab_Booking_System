package com.i2i.zapcab.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import static com.i2i.zapcab.constant.ZapCabConstant.*;

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

