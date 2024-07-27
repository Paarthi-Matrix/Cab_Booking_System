package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing customer profile details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the customer. </li>
 *       <li> Email address of the customer. </li>
 *       <li> Mobile number of the customer. </li>
 *       <li> Gender of the customer. </li>
 *       <li> Tier of the customer (e.g., standard, premium). </li>
 *   </ol>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerProfileDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String gender;
    private String tier;
}
