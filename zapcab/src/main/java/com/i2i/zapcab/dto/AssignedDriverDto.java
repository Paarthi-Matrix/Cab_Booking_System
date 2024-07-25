package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedDriverDto {
   private String Otp;
   private String name;
   private String mobileNumber;
   private int ratings;
   private String category;
   private String model;
   private String licensePlate;
}
