package com.i2i.zapcab.service;

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
    public Driver saveDriver(Driver driver);

    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto);

    /**
     * This method is used to get the ride details along with the customer details
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto Holds the ride details
     */
    RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto);

    Driver getByMobileNumber(String mobileNumber);

   // public void updateDriverStatus(UpdateDriverStatusDto updateDriverStatusDto);
    void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto);
    void changePassword(String id, ChangePasswordRequestDto changePasswordRequestDto);
    boolean updateDriverRating(int id, int ratings);
}
