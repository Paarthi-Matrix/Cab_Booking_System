package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Customer;

/**
 * <p>
 * An interface fo managing the customer's operation
 * Th operation includes :
 *     <ul>
 *         <li> Fetching vehicles and calculating fare </li>
 *         <li> Updating the driver's rating </li>
 *         <li> Assigning the rides to the driver </li>
 *     </ul>
 * </p>
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
     * @throws DatabaseException if an error occurs while saving the ride request.
     */
    boolean saveRideRequest(String id, RideRequestDto rideRequestDto);

    /**
     * <p>
     * This method is used to save the customer to the repository.
     * </p>
     *
     * @param customer {@link Customer}
     * @throws DatabaseException {@link DatabaseException}
     *                           Thrown while saving customer entity to the repository.
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
     * @throws DatabaseException if an error occurs while updating the ratings.
     */
    boolean updateRideAndDriverRating(String id, RideRatingDto ratings);

    /**
     * <p>
     * Retrieves the assigned driver details for a given ride request ID.
     * </p>
     *
     * @param id The unique id of the ride request.
     * @return An AssignedDriverDto object containing the driver's details if the ride request is assigned, otherwise null.
     * @throws DatabaseException if an error occurs while fetching the assigned driver details.
     */
    AssignedDriverDto getAssignedDriverDetails(String id);

    /**
     * <p>
     * Retrieves all ride history records for a specific user by their ID.
     * Converts the ride history entities to DTOs before returning them.
     * </p>
     *
     * @param id The ID of the user whose ride history is to be retrieved.
     * @return A list of RideHistoryResponseDto objects representing the user's ride history.
     * @throws DatabaseException If an error occurs while fetching the ride history.
     */
    List<RideHistoryResponseDto> getAllRideHistoryById(String id);

    /**
     * <p>
     * Fetches the profile of a customer based on their user ID.
     * </p>
     *
     * @param userId The ID of the customer
     * @return {@link  CustomerProfileDto}
     * The profile information of the customer.
     * @throws DatabaseException If error occurs while retrieving the customer profile.
     */
    CustomerProfileDto getCustomerProfile(String userId);

    /**
     * <p>
     * Updates the tier of a customer based on the user ID.
     * </p>
     *
     * @param userId  The userId of the customer.
     * @param tierDto The newTier to be set for the customer.
     * @throws DatabaseException If error occurs while updating the customer tier.
     */
    void updateCustomerTier(String userId, TierDto tierDto);

    /**
     * <p>
     * Retrieves the customer ID associated with a given user ID.
     * </p>
     *
     * @param userId The user id for whom the customer ID needs to be retrieved.
     * @return String
     * The customer ID associated with the given user ID.
     * @throws DatabaseException If error occurs while retrieving the customer ID.
     */
    String retrieveCustomerIdByUserId(String userId);

    /**
     * <p>
     * Updates the tier of a customer based on the user ID.
     * </p>
     *
     * @param userId The userId of the customer.
     * @return TierDto
     * The updated tier information of the customer.
     * @throws DatabaseException If error occurs while updating the customer tier.
     */
    TierDto getCustomerTier(String userId);
}