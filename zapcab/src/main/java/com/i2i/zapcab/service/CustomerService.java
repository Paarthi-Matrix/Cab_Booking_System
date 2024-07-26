package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;


/**
 * <p>
 *     An interface fo managing the customer's operation
 *     Th operation includes :
 *     <ul>
 *         <li> Fetching vehicles and calculating fare </li>
 *         <li> Updating the driver's rating </li>
 *         <li> Assigning the rides to the driver </li>
 *     </ul>
 * </p>
 *
 */

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

    boolean updateRideAndDriverRating(String id, RideRatingDto ratings);

    AssignedDriverDto getAssignedDriverDetails(String id);

    public CustomerProfileDto getCustomerProfile(String customerId);

    public void updateCustomerTier(String userId, String newTier);
}