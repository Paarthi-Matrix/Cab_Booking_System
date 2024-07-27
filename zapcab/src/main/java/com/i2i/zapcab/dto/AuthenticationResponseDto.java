package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing authentication response details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Authentication token generated upon successful login. </li>
 *   </ol>
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    private String token;
}
