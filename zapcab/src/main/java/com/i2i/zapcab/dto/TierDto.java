package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * This class is responsible for managing tier information.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Tier designation or level for a user or service. </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@AllArgsConstructor
public class TierDto {
    private String tier;
}
