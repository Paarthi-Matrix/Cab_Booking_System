package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import lombok.Synchronized;
import org.hibernate.annotations.Synchronize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DriverService {
    public Driver saveDriver(Driver driver);

    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto);

    public RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto);

    public Driver getByMobileNumber(String mobileNumber);

    public void updateDriverStatus(UpdateDriverStatusDto updateDriverStatusDto);

    boolean updateDriverRating(int id, int ratings);
}
