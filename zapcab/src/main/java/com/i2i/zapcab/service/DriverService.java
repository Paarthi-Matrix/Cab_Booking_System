package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;

import com.i2i.zapcab.model.Driver;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public interface DriverService {
    public Driver saveDriver(Driver driver);

    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto);

    /**
     * This method is used to get the ride details along with the customer details
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto Holds the ride details
     */

    public RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto);

   public Driver getByMobileNumber(String mobileNumber);

   // public void updateDriverStatus(UpdateDriverStatusDto updateDriverStatusDto);

    void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto);

    void changePassword(String id, String newPassword);

    boolean updateDriverRating(int id, int ratings);
}
