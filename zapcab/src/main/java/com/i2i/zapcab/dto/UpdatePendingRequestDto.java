package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePendingRequestDto {
    private String phoneNumber;
    private String status;
    private String remarks;
}
