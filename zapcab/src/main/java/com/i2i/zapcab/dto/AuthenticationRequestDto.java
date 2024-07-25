package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     This class represents a Data Transfer Object (DTO) for authentication requests.
 *     It contains the phone number and password required for user authentication.
 *
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    private String phoneNumber;
    private String password;
}
