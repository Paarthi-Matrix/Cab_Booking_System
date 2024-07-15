package com.i2i.zapcab.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Builder
@Getter
@Setter
public class RegisterCustomerDto {
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only alphabetic characters")
    private String name;

    @NotNull(message = "Contact number is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Contact number should be a valid number")
    private Long contactNumber;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.com$", message = "Email should contain .com and @")
    private String email;

    @NotBlank(message = "Gender is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Gender must contain only alphabetic characters")
    private String gender;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should have at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one letter, one number, and one special character")
    private String password;
}

