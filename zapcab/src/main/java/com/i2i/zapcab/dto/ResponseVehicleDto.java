package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseVehicleDto {
    private int id;
    private String category;
    private String type;
    private String model;
    private String licensePlate;
    private int maxSeats;
    private String status;
}
