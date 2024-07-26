package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.OTPResponseDto;
import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.dto.RideInvoiceDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;

/**
 * <p>
 *     An interface that manages the operations in {@link Ride} entity
 * </p>
 */
@Component
public interface RideService {
    /**
     *
     * @param rideRequest
     * @param driver
     */
    public void saveRide(RideRequest rideRequest, Driver driver);

    String updateRideRating(String id, RideRatingDto ratings);

    Ride getRideByRideRequest(String id);
    /**
     * <p>
     *     Updates the status of an existing ride.
     * </p>
     * @param id
     *        The ID of the ride whose status needs to be updated.
     * @param statusDto {@link StatusDto}
     *        The StatusDto object containing the new status of the ride.
     * @return RideResponseDto
     *         The response object containing the updated ride details.
     */
    public RideResponseDto updateRideStatus(String id, StatusDto statusDto);

    /**
     * <p>
     *     Tracks the status of an existing ride.
     * </p>
     * @param id
     *        The ID of the ride whose status needs to be tracked.
     * @return RideResponseDto
     *         The response object containing the current ride status.
     */
    public RideResponseDto trackRideStatus(String id);

    /**
     * <p>
     *     Generates an invoice for a completed ride.
     * </p>
     * @param rideId
     *        The ID of the ride for which the invoice is to be generated.
     * @return RideInvoiceDto
     *         The response object containing the ride invoice details.
     */
    public RideInvoiceDto generateRideInvoice(String rideId);

    public PaymentModeDto paymentMode(String id, PaymentModeDto paymentModeDto);
}
