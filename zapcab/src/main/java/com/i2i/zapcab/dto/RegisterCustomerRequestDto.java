package com.i2i.zapcab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.i2i.zapcab.helper.RoleEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterCustomerRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private List<RoleEnum> role;
}
