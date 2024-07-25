package com.i2i.zapcab.mapper;

import static com.i2i.zapcab.common.ZapCabConstant.RIDE_BOOKED;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;

public class RideMapper {
    public Ride rideRequestToRide(RideRequest rideRequest, Driver driver) {
        return Ride.builder().rideRequest(rideRequest).
                distance(rideRequest.getDistance()).
                status(RIDE_BOOKED).
                fare(rideRequest.getFare()).
                dropPoint(rideRequest.getDropPoint()).
                driver(driver).build();
    }
}
