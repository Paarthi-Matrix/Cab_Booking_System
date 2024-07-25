package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;
import com.i2i.zapcab.dto.ChangePasswordRequestDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;

import com.i2i.zapcab.model.Driver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * <p>
 *    This interface includes methods for saving a driver, updating a driver's status and location,
 *    and changing a driver's password. Implementations of this interface will provide the actual
 *    business logic for these operations.
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
     * Fetches the particular ride detail which the driver selected from the list
     * </p>
     *
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto Holds the ride details
     * {@link RideDetailsDto}
     */
    public RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto);

    /**
     * <p>
     * Fetches the particular driver details by giving mobile number
     * </p>
     *
     * @param mobileNumber valid 10 digit mobile number
     * @return Driver {@link Driver}
     */
    public Driver getByMobileNumber(String mobileNumber);

   // public void updateDriverStatus(UpdateDriverStatusDto updateDriverStatusDto);

    void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto);

    void changePassword(String id, String newPassword);

    boolean updateDriverRating(int id, int ratings);

    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, MaskMobileNumberRequestDto maskMobileNumberRequestDto);

    public Boolean otpValidation(OtpRequestDto otpRequestDto);
}
