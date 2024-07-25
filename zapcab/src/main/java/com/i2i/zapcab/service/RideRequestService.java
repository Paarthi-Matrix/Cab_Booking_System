package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RideRequestService {
    boolean saveRideRequest(Customer customer,RideRequestDto rideRequestDto);

    public List<RideRequest> getAll();

    public RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto);

    void updateRideRequestStatus(int id);

    RideRequest checkStatusAssignedOrNot(String id);
}
