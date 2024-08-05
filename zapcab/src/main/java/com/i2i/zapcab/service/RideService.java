package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.RideStatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import java.util.Optional;

/**
 * <p>
 * An interface that manages the operations in {@link Ride} entity
 * </p>
 */
@Component
public interface RideService {

    /**
     * This method is used to save the ride details that are requested by customer
     *
     * @param rideRequest {@link RideRequest}
     * @param driver      {@link Driver}
     */
    void saveRide(RideRequest rideRequest, Driver driver);

    /**
     * <p>
     * Updates the rating of a ride with the given ID and returns the driver's ID.
     * </p>
     *
     * @param id      The unique id of the ride.
     * @param ratings The data transfer object containing the new ride rating.
     * @return The ID of the driver associated with the ride.
     * @throws DatabaseException if an error occurs while updating the ride rating.
     */
    String updateRideRating(String id, RideRatingDto ratings);

    /**
     * <p>
     * Retrieves a ride based on the provided ride request ID.
     * </p>
     *
     * @param id The unique id of the ride request.
     * @return The Ride object associated with the provided ride request ID.
     * @throws DatabaseException if an error occurs while retrieving the ride.
     */
    Ride getRideByRideRequest(String id);

    public RideResponseDto updateRideStatus(String id, RideStatusDto rideStatusDto);

    /**
     * <p>
     * Updates the status of an existing ride.
     * </p>
     *
     * @param id The ID of the ride whose status needs to be updated.
     * @throws DatabaseException If error occurs while updating the status.
     * @return Ride {@link Ride}
     */
    String updateRideStatus(String id);

    /**
     * <p>
     * Tracks the status of an existing ride.
     * </p>
     *
     * @param driverId The ID of the ride whose payment mode to be updated.
     * @return RideResponseDto {@link  RideResponseDto}
     * @throws DatabaseException If error occurs while updating the payment mode details.
     */
    RideResponseDto setPaymentMode(String driverId, PaymentModeDto paymentModeDto);

    Optional<Ride> getRideById(String id);

    void softDeleteRide(String rideId);
}