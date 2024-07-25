package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FetchAllPendingRequestsDto {
    private String name;
    private String email;
    private String licenseNo;
    private String mobileNumber;
    private String city;
    private String rcBookNo;
    private LocalDate dob;
    private String status;
    private String category;
    private String model;
    private String licensePlate;
    private String remarks;

}
