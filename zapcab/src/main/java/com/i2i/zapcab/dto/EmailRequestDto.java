package com.i2i.zapcab.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailRequestDto {
    String driverName;
    String mobilNumber;
    String password;
    String toEmail;
}
