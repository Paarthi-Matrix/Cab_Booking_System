package com.i2i.zapcab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * This class is responsible for managing the details of all pending requests.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the requester. </li>
 *       <li> Email address of the requester. </li>
 *       <li> License number associated with the request. </li>
 *       <li> Mobile number of the requester. </li>
 *       <li> City of residence of the requester. </li>
 *       <li> RC book number associated with the request. </li>
 *       <li> Date of birth of the requester. </li>
 *       <li> Status of the request. </li>
 *       <li> Category of the vehicle (e.g., sedan, SUV). </li>
 *       <li> Model of the vehicle. </li>
 *       <li> License plate number of the vehicle. </li>
 *       <li> Additional remarks related to the request. </li>
 *   </ol>
 * </p>
 */
@Getter
@Setter
@Builder
public class FetchAllPendingRequestsDto {
    private String name;
    private String email;
    private String licenseNo;
    private String mobileNumber;
    private String city;
    private String rcBookNo;
    private LocalDate dob;
    private String status;
    private String category;
    private String model;
    private String licensePlate;
    private String remarks;

}
