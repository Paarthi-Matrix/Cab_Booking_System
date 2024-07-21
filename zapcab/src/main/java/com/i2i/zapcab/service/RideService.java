package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.OTPResponseDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;

@Component
public interface RideService {

    public void saveRide(RideRequest rideRequest, Driver driver);

    int updateRideRating(int id, RideRatingDto ratings);

    Ride getRideByRideRequest(int id);
    RideResponseDto getRide(int id);
    RideResponseDto updateRideStatus(int id, StatusDto statusDto);
    RideResponseDto updateRide(int id, RideRequestDto rideRequestDto);
    String trackRideStatus(int id);
}
