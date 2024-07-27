package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing status information.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Status message or code indicating the result of an operation. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private String status;
}
