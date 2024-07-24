package com.i2i.zapcab.mapper;

import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;

public class RideMapper {
    public Ride rideRequestToRide(RideRequest rideRequest, String mobileNumber, Driver driver) {
        return Ride.builder().rideRequest(rideRequest).
                distance(rideRequest.getDistance()).
                fare(rideRequest.getFare()).
                dropPoint(rideRequest.getDropPoint()).
                driver(driver).build();
    }
}
