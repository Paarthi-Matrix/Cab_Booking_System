package com.i2i.zapcab.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.i2i.zapcab.common.ZapCabConstant.*;

/**
 * <p>
 * This class is responsible for managing the details required to register a new driver.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Name of the driver. </li>
 *       <li> Region where the driver operates. </li>
 *       <li> City where the driver operates. </li>
 *       <li> Mobile number of the driver. </li>
 *       <li> Email address of the driver. </li>
 *       <li> License number of the driver. </li>
 *       <li> RC book number of the vehicle. </li>
 *       <li> Date of birth of the driver. </li>
 *       <li> Gender of the driver. </li>
 *       <li> License plate of the vehicle. </li>
 *       <li> Category of the vehicle (e.g., sedan, SUV). </li>
 *       <li> Type of the vehicle (e.g., electric, hybrid). </li>
 *       <li> Model of the vehicle. </li>
 *   </ol>
 * </p>
 * <p>
 * <b>Validation Constraints:</b>
 *   <ul>
 *       <li> Name: Must not be blank and should match the specified pattern. </li>
 *       <li> Region: Must not be blank and should match the specified pattern. </li>
 *       <li> City: Must not be blank and should match the specified pattern. </li>
 *       <li> Mobile Number: Must not be blank and should match the specified pattern. </li>
 *       <li> Email: Must not be blank, must be a valid email format, and should match the specified pattern. </li>
 *       <li> License Number: Must not be blank and should match the specified pattern. </li>
 *       <li> RC Book Number: Must not be blank. </li>
 *       <li> Date of Birth: Must not be null and should be a past date. </li>
 *       <li> Gender: Must not be blank and should match the specified pattern. </li>
 *       <li> License Plate: Must not be blank and should match the specified pattern. </li>
 *   </ul>
 * </p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDriverRequestDto {
    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = NAME_PATTERN_MESSAGE)
    private String name;

    @NotBlank(message = REGION_NOT_BLANK)
    @Pattern(regexp = STRING_AND_NUMBER_REGEX, message = REGION_PATTERN_MESSAGE)
    private String region;

    @NotBlank(message = REGION_NOT_BLANK)
    @Pattern(regexp = STRING_REGEX, message = REGION_PATTERN_MESSAGE)
    private String city;

    @NotBlank(message = MOBILE_NUMBER_NOT_BLANK)
    @Pattern(regexp = MOBILE_NUMBER_REGEX, message = MOBILE_NUMBER_PATTERN_MESSAGE)
    private String mobileNumber;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_PATTERN_MESSAGE)
    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_PATTERN_MESSAGE)
    private String email;

    @NotBlank(message = LICENSE_NUMBER_NOT_BLANK)
    @Pattern(regexp = LICENSE_NUMBER_REGEX, message = LICENSE_NUMBER_MESSAGE)
    private String licenseNumber;

    @NotBlank(message = RC_BOOK_NOT_BLANK)
    private String rcBookNo;

    @NotNull(message = DOB_NOT_NULL)
    @Past(message = DOB_PAST_MESSAGE)
    private LocalDate dateOfBirth;

    @NotBlank(message = GENDER_NOT_BLANK)
    @Pattern(regexp = VALID_GENDER, message = GENDER_PATTERN_MESSAGE)
    private String gender;

    @NotBlank(message = LICENSE_PLATE_NOT_BLANK)
    @Pattern(regexp = LICENSE_PLATE_REGEX)
    private String licensePlate;
    private String category;
    private String type;
    private String model;
}

