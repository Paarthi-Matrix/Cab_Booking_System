package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Builder
@Getter
@Setter
public class UpdateDriverPasswordDto {
    @NotNull(message = "Contact number is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Contact number should be a valid number")
    private Long contactNumber;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should have at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one letter, one number, and one special character")
    private String password;
}
