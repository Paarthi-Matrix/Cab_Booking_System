package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
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
    List<RideRequestResponseDto> getAvailableVehiclesWithFare(CheckVehicleAvailabilityDto checkVehicleAvailabilityDto);

    RideRequest saveRideRequest(int id,RideRequestDto rideRequestDto);
    void saveCustomer(Customer customer);
    boolean updateDriverRating(int id, RideRatingDto ratings);
}