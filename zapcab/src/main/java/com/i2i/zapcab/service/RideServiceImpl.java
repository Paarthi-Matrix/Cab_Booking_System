package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideServiceImpl implements RideService {
    @Autowired
    RideRepository rideRepository;

    @Override
    public void saveRide(RideRequest rideRequest, String mobileNumber, Driver driver) {
        Ride ride = Ride.builder().status("Booked")
                .rideRequest(rideRequest)
                .distance(rideRequest.getDistance())
                .fare(rideRequest.getFare())
                .dropPoint(rideRequest.getDropPoint())
                .driver(driver)
                .build();
        rideRepository.save(ride);
    }

    @Override
    public int updateRideRating(int id, RideRatingDto ratings) {
        try {
            Ride ride = rideRepository.findById(id).get();
            ride.setRideRating(ratings.getRatings());
            rideRepository.save(ride);
            return ride.getDriver().getId();
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while updating ride rating with its id: " + id, e);
        }
    }

    @Override
    public Ride getRideByRideRequest(int id) {
        try {
            return rideRepository.findRideByRideRequestId(id);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while get ride by ride request id:" + id, e);
        }
    }
}
