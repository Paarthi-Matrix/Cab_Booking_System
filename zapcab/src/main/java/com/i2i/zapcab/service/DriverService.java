package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.*;

import com.i2i.zapcab.model.Driver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This interface includes methods for saving a driver, updating a driver's status and location,
 * and changing a driver's password. Implementations of this interface will provide the actual
 * business logic for these operations.
 * </p>
 */
@Component
public interface DriverService {
    /**
     * <p>
     * This method used to save the user as a driver once the admin approves
     * by the background verification.
     * </p>
     *
     * @param driver {@link Driver}
     * @return Driver - holds the driver details
     */
    public Driver saveDriver(Driver driver);

    /**
     * <p>
     * This functionality is used to fetch the ride requested by the customers
     * according to the following filtration.
     *     <ol>
     *         <li> Vehicle category filtration </li>
     *         <li> Current location of the vehicle </li>
     *     </ol>
     * </p>
     *
     * @param getRideRequestListsDto
     * @return List<RequestedRideDto>
     * Returns the list of rides to the driver
     */

    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto);

    /**
     * <p>
     *     Fetches the particular ride detail which the driver selected from the list
     * </p>
     *
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto Holds the ride details
     */
    RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto);

    /**
     * <p>
     * Fetches the particular driver details by giving mobile number
     * </p>
     *
     * @param mobileNumber valid 10 digit mobile number
     * @return Driver {@link Driver}
     */
    Driver getByMobileNumber(String mobileNumber);

    void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto);

    void changePassword(String id, String newPassword);

    /**
     * <p>
     * Updates the rating of a driver with the given ID.
     * </p>
     *
     * @param id      The unique id of the driver.
     * @param ratings The new rating to be added to the driver's current rating.
     * @return true if the driver's rating is updated successfully, otherwise false.
     * @throws com.i2i.zapcab.exception.UnexpectedException if an error occurs while updating the driver's rating.
     */
    boolean updateDriverRating(String id, int ratings);

    /**
     * <p>
     *     This method is used to mask the driver's mobile number
     *     if he/she wishes not to expose the mobile number.
     * </p>
     * @param id
     *      User's unique id
     * @param maskMobileNumberRequestDto {@link MaskMobileNumberResponseDto}
     * @return
     */
    MaskMobileNumberResponseDto updateMaskMobileNumber(String id, MaskMobileNumberRequestDto maskMobileNumberRequestDto);

    /**
     * <p>
     *     This method is used to verifies the otp given by the customer
     * </p>
     * @param otpRequestDto {@link OtpRequestDto}
     * @return Boolean
     *       Returns true if the otp is correct otherwise return false.
     */
    public Boolean otpValidation(OtpRequestDto otpRequestDto);
    /**
     *
     */
    void updateDriverWallet(String id, String paymentMode, String rideStatus, int fare);
}

