package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     This class represents a Data Transfer Object (DTO) for searching the available nearby drivers.
 *     It contains information about the driver, including their name, mobile number,
 *     license plate, location, category, and type.
 * </p>
 */
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
