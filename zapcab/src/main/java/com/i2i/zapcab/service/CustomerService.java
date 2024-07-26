package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.History;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

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
     * @param checkVehicleAvailabilityDto Data transfer object containing the
     *                                    pickup and drop points.
     * @return A VehicleAvailabilityResponseDto containing the pickup and drop point and
     * also containing the available vehicles with their respective fares.
     */
    VehicleAvailabilityResponseDto getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto);

    /**
     * <p>
     * Saves a ride request for a given user.
     * </p>
     *
     * @param id             The unique id of the user.
     * @param rideRequestDto The data transfer object containing ride request details.
     * @return true if the ride request is saved successfully, otherwise false.
     * @throws com.i2i.zapcab.exception.UnexpectedException if an error occurs while saving the ride request.
     */
    boolean saveRideRequest(String id, RideRequestDto rideRequestDto);

    /**
     * <p>
     * This method is used to save the customer to the repository.
     * </p>
     *
     * @param customer {@link Customer}
     * @throws UnexpectedException {@link UnexpectedException}
     *                             Thrown while saving customer entity to the repository.
     */
    void saveCustomer(Customer customer);

    /**
     * <p>
     * Updates the ride and driver ratings for a given ride.
     * </p>
     *
     * @param id      The unique id of the ride.
     * @param ratings The data transfer object containing the ride and driver ratings.
     * @return true if the ratings are updated successfully, otherwise false.
     * @throws com.i2i.zapcab.exception.UnexpectedException if an error occurs while updating the ratings.
     */
    boolean updateRideAndDriverRating(String id, RideRatingDto ratings);

    /**
     * <p>
     * Retrieves the assigned driver details for a given ride request ID.
     * </p>
     *
     * @param id The unique id of the ride request.
     * @return An AssignedDriverDto object containing the driver's details if the ride request is assigned, otherwise null.
     * @throws com.i2i.zapcab.exception.UnexpectedException if an error occurs while fetching the assigned driver details.
     */
    AssignedDriverDto getAssignedDriverDetails(String id);

    public CustomerProfileDto getCustomerProfile(String customerId);

    public void updateCustomerTier(String userId, String newTier);
}