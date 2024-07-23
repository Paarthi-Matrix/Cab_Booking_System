package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideServiceImpl implements RideService{
    @Autowired
    RideRepository rideRepository;

    @Override
    public void saveRide(RideRequest rideRequest, String mobileNumber, Driver driver) {
        //Driver driver = driverService.getByMobileNumber(mobileNumber);
        Ride ride = Ride.builder().status("Booked")
                .rideRequest(rideRequest)
                .distance(rideRequest.getDistance())
                .fare(rideRequest.getFare())
                .dropPoint(rideRequest.getDropPoint())
                .driver(driver)
                .build();
        rideRepository.save(ride);
    }
    public int updateRideRating(int id, RideRatingDto ratings){
        Ride ride = rideRepository.findById(id).get();
        ride.setRideRating(ratings.getRatings());
        rideRepository.save(ride);
        return ride.getDriver().getId();
    }

}
