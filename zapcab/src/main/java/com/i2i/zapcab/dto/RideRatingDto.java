package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class is responsible for managing the details of a ride rating.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Rating given for the ride, typically on a scale (e.g., 1 to 5). </li>
 *   </ol>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRatingDto {
    private int ratings;
}
