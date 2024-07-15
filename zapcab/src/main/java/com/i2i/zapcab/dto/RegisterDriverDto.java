package com.i2i.zapcab.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterDriverDto {
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only alphabetic characters")
    private String name;

    @NotBlank(message = "Region is mandatory")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only alphabetic characters")
    private String region;

    @NotNull(message = "Contact number is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Contact number should be a valid number")
    private Long contactNumber;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.com$", message = "Email should contain .com and @")
    private String email;

    @NotBlank(message = "License number is mandatory")
    private String licenseNo;

    @NotBlank(message = "RC Book number is mandatory")
    private String rcBookNo;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(message = "License plate is mandatory")
    private String licensePlate;
}

