package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.model.RideRequest;
import org.hibernate.id.Assigned;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerService {
    /**
     * <p>
     * Fetches available vehicles for the given pickup point and
     * calculates the fare for each vehicle.
     * </p>
     *
     * @param checkVehicleAvailabilityDto Data transfer object containing the pickup and drop points.
     * @return A list of RideRequestResponseDto containing the available vehicles with their respective fares.
     */
    VehicleAvailabilityResponseDto getAvailableVehiclesWithFare(CheckVehicleAvailabilityDto checkVehicleAvailabilityDto);

    RideRequest saveRideRequest(String id,RideRequestDto rideRequestDto);

    void saveCustomer(Customer customer);

    boolean updateRideAndDriverRating(int id, RideRatingDto ratings);

    AssignedDriverDto getAssignedDriverDetails(String id);
}