package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDriverDto {
    private String name;
    private String mobileNumber;
    private String licensePlate;
    private String location;
    private String category;
    private String type;
}
