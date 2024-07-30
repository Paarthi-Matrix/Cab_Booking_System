package com.i2i.zapcab.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.i2i.zapcab.helper.RoleEnum;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterCustomerRequestDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String gender;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private List<RoleEnum> role;
}
