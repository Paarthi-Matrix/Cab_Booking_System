package com.i2i.zapcab.service;

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
    /**
     *
     * @param customer
     * @param rideRequestDto
     * @return
     */
    RideRequest saveRideRequest(Customer customer,RideRequestDto rideRequestDto);

    public List<RideRequest> getAll();

    public RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto);

    void updateRequest(RideRequest rideRequest);

    boolean saveRideRequests(Customer customer, RideRequestDto rideRequestDto);

    void updateRideRequestStatus(String id);

    RideRequest checkStatusAssignedOrNot(String id);

    public UpdateResponseDto updateRideDetails(String id, UpdateRideDto updateRideDto);
}
