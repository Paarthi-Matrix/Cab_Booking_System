package com.i2i.zapcab.dto;
import com.i2i.zapcab.helper.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateDriverDto {
    private String name;
    private String city;
    private String email;
    private String region;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String licenseNumber;
    private String rcBookNumber;
    private List<RoleEnum> role;
    private String category;
    private String type;
    private String model;
    private String licensePlate;
}