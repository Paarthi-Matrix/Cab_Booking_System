package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;


@Component
public interface RideRequestService {
    RideRequest saveRideRequest(Customer customer,RideRequestDto rideRequestDto);
    List<RideRequest> getAll();
    RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto);
}
