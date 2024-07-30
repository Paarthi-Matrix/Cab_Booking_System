package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Driver;


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
     * @param getRideRequestListsDto {@link GetRideRequestListsDto}
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
     * {@link RideDetailsDto}
     */
    public RideDetailsDto acceptRide(DriverSelectedRideDto selectedRideDto, String id);

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
     * @throws DatabaseException if an error occurs while updating the driver's rating.
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
     * @return MaskMobileNumberResponseDto
     *      A message to the user
     * @throws DatabaseException
     *      Occurs whenever if the number is not masked
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
    public Boolean otpValidation(OtpRequestDto otpRequestDto, String id);
    /**
     *<p>
     *     Updates the driver's wallet according to the following criteria
     *     <ul>
     *         <li> Payment Mode -  If it is cash payment, the 20% amount will be reduced from the wallet</li>
     *         <li> If the wallet is 0 or less, the driver has to recharge</li>
     *     </ul>
     *</p>
     * @param id
     *      User's unique id
     * @param paymentMode
     *      Specifies the mode of the payment that customer does
     * @param fare
     *      Specifies the amount of that particular ride
     */
    void updateDriverWallet(String id, String paymentMode, String rideStatus, int fare);

    /**
     * <p>
     *     Retrieves the driver ID associated with a given user ID.
     * </p>
     *
     * @param userId
     *      The user id for whom the driver ID needs to be retrieved.
     * @return String
     *      The driver ID associated with the given user ID.
     * @throws DatabaseException
     *       If error occurs while retrieving the driver ID.
     */
    String retrieveDriverIdByUserId(String userId);
}