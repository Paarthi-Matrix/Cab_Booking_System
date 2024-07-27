package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing the details of ride request lists.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Category of the ride request (e.g., sedan, SUV). </li>
 *       <li> Location associated with the ride request. </li>
 *   </ol>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetRideRequestListsDto {
    private String category;
    private String location;
}
