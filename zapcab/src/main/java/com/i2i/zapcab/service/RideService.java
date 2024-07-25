package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;

@Component
public interface RideService {
    void saveRide(RideRequest rideRequest, String mobileNumber, Driver driver);
    int updateRideRating(int id, RideRatingDto ratings);
    Ride getRideByRideRequest(int id);
}
