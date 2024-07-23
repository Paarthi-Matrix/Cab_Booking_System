package com.i2i.zapcab.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserResponseDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String gender;
    private String password;
    private LocalDate dateOfBirth;
}
