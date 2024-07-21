package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.mapper.RideMapper;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideServiceImpl implements RideService{
    private static final Logger logger  = LogManager.getLogger(RideServiceImpl.class);
    private RideMapper rideMapper = new RideMapper();
    @Autowired
    private RideRepository rideRepository;


    @Override
    public void saveRide(RideRequest rideRequest, Driver driver) {
        try {
            logger.info("Saving the request {} to the ride table....",rideRequest.getPickupPoint());
            Ride ride = rideMapper.rideRequestToRide(rideRequest, driver);
            rideRepository.save(ride);
            logger.info("Successfully ride has been saved {}",rideRequest.getPickupPoint());
        } catch(Exception e) {
            logger.error("Unable to save the ride requested by the customer : {}",
                    rideRequest.getCustomer().getUser().getName());
            throw new UnexpectedException("Unable to save the ride for the request : "+ rideRequest.getId(), e);
        }
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

    @Override
    public RideResponseDto getRide(int id) {
        return null;
    }

    @Override
    public RideResponseDto updateRideStatus(int id, StatusDto statusDto) {
        return null;
    }

    @Override
    public RideResponseDto updateRide(int id, RideRequestDto rideRequestDto) {
        return null;
    }

    @Override
    public String trackRideStatus(int id) {
        return "";
    }
}