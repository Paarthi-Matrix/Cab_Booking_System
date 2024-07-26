package com.i2i.zapcab.service;

import java.util.List;

import com.i2i.zapcab.exception.UnexpectedException;
import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.UpdateResponseDto;
import com.i2i.zapcab.dto.UpdateRideDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * An interface that manages the ride request operation made by the customer.
 */
@Component
public interface RideRequestService {
    public List<RideRequest> getAll();

    public RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto);

    void updateRequest(RideRequest rideRequest);
    /**
     * <p>
     * Saves a ride request for a given customer.
     * </p>
     *
     * @param customer       The customer requesting the ride.
     * @param rideRequestDto The data transfer object containing ride request details.
     * @return true if the ride request is saved successfully, otherwise false.
     * @throws UnexpectedException if an error occurs while saving the ride request.
     */
    boolean saveRideRequest(Customer customer,RideRequestDto rideRequestDto);

    /**
     * <p>
     * Updates the status of a ride request to "ASSIGNED" for a given ride request ID.
     * </p>
     *
     * @param id The unique id of the ride request.
     * @throws UnexpectedException if an error occurs while updating the ride request status.
     */
    void updateRideRequestStatus(String id);

    /**
     * <p>
     * Checks if the ride request for a given customer ID is assigned or not.
     * </p>
     *
     * @param id The unique identifier of the customer.
     * @return The RideRequest if found, otherwise null.
     * @throws UnexpectedException if an error occurs while checking the ride request status.
     */
    RideRequest checkStatusAssignedOrNot(String id);

    public UpdateResponseDto updateRideDetails(String id, UpdateRideDto updateRideDto);
}
