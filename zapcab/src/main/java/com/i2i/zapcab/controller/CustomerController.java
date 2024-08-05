package com.i2i.zapcab.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideStatusDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.dto.UpdateRideDto;
import com.i2i.zapcab.dto.UpdateRideResponseDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.CustomerService;
import com.i2i.zapcab.service.RideRequestService;
import com.i2i.zapcab.service.UserService;

/**
 * <p>
 * The CustomerController class handles various HTTP requests related to customer operations
 * in the ZapCab application. This includes fetching available vehicles, saving ride requests,
 * updating ride and driver ratings, retrieving assigned driver details, fetching ride history,
 * deleting customer profiles, fetching customer profiles and tiers, and updating ride details.
 * <p>
 * The controller leverages several services to perform these operations, including CustomerService,
 * RideRequestService, and UserService. JWT tokens are used to authenticate and extract user information.
 * </p>
 */
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private RideRequestService rideRequestService;
    @Autowired
    private UserService userService;

    /**
     * <p>
     * This method handles the request to Fetch available vehicles for the given pickup point and
     * calculates the fare for each vehicle.
     * </p>
     *
     * @param checkVehicleAvailabilityDto {@link CheckVehicleAvailabilityDto}
     * @return A VehicleAvailabilityResponseDto containing the pickup and drop point and also containing the available vehicles with
     * their respective fares.
     */
    @GetMapping("/me/vehicles")
    public ApiResponseDto<?> getAvailableVehiclesWithFare(
            @RequestBody CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        try {
            logger.info("Received request to check vehicle availability with details: {}"
                    , checkVehicleAvailabilityDto);
            VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = customerService.
                    getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
            if (!ObjectUtils.isEmpty(vehicleAvailabilityResponseDto)) {
                logger.info("Available vehicles found: {}", vehicleAvailabilityResponseDto);
                return ApiResponseDto.statusOk(vehicleAvailabilityResponseDto);
            } else {
                logger.warn("Same pickup and drop point not allowed: {}", checkVehicleAvailabilityDto);
                return ApiResponseDto.statusBadRequest("Same pickup and drop point not allowed");
            }
        } catch (DatabaseException e) {
            logger.error("Database error occurred while checking vehicle availability: ", e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method handles the request to Save a ride request for a given user.
     * </p>
     *
     * @param rideRequestDto The data transfer object containing ride request details.
     * @return {@link ApiResponseDto}
     */
    @PostMapping("/me/rideRequest")
    public ApiResponseDto<String> saveRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Received ride request from user ID: {}, details: {}", id, rideRequestDto);
            if (customerService.saveRideRequest(id, rideRequestDto)) {
                logger.info("Ride request saved successfully for user ID: {}", id);
                return ApiResponseDto.statusOk("Searching For Captain to Accept...");
            } else {
                logger.warn("Ride request not found for user ID: {}", id);
                return ApiResponseDto.statusNotFound(null);
            }
        } catch (DatabaseException e) {
            logger.error("Database error occurred while saving ride request for user ID: " + id, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method handles the request to Update the ride and driver ratings for a given ride.
     * </p>
     *
     * @param id      The unique id of the ride.
     * @param ratings The data transfer object containing the ride and driver ratings.
     * @return {@link ApiResponseDto}
     */
    @PatchMapping("me/rides/{id}")
    public ApiResponseDto<String> updateRideAndDriverRating(@PathVariable String id, @RequestBody RideRatingDto ratings) {
        try {
            logger.info("Received request to update ride and driver rating for ride ID: {}, with ratings: {}", id, ratings);
            if (customerService.updateRideAndDriverRating(id, ratings)) {
                logger.info("Ratings updated successfully for ride ID: {}", id);
                return ApiResponseDto.statusOk("Ratings updated successfully");
            } else {
                logger.warn("Ride not found for ride ID: {}", id);
                return ApiResponseDto.statusNotFound(null);
            }
        } catch (DatabaseException e) {
            logger.error("Database error occurred while updating ratings for ride ID: " + id, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method handles the request to Retrieve the assigned
     * driver details for a given ride request ID.
     * </p>
     *
     * @return {@link AssignedDriverDto}
     * if the ride request is assigned, otherwise {@link ApiResponseDto}.
     */
    @GetMapping("/me/driver")
    public ApiResponseDto<?> getAssignedDriverDetails() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Fetching assigned driver details for customer ID: {}", id);
            AssignedDriverDto assignedDriverDto = customerService.getAssignedDriverDetails(id);
            if (!ObjectUtils.isEmpty(assignedDriverDto)) {
                logger.info("Assigned driver details found for customer ID: {}", id);
                return ApiResponseDto.statusOk(assignedDriverDto);
            } else {
                logger.info("No assigned driver found for customer ID: {}. Searching for captain to accept request.", id);
                return ApiResponseDto.statusAccepted("Waiting for captain to accept request");
            }
        } catch (DatabaseException e) {
            logger.error("Error occurred while fetching assigned driver details for customer ID: {}. Error: {}",
                    id, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }
    /**
     * <p>
     * This method handles the request to Retrieve all ride history records
     * for a specific user by their ID.
     * Converts the ride history entities to DTOs before returning them.
     * </p>
     *
     * @return A list of RideHistoryResponseDto {@link RideHistoryResponseDto}
     */
    @GetMapping("/me/rides")
    public ApiResponseDto<?> getAllRideHistory() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Fetching ride history for customer ID: {}", id);
            List<RideHistoryResponseDto> rideHistoryResponseDtos = customerService.getAllRideHistoryById(id);
            if (!rideHistoryResponseDtos.isEmpty()) {
                logger.info("Ride history found for customer ID: {}", id);
                return ApiResponseDto.statusOk(rideHistoryResponseDtos);
            } else {
                logger.info("No ride history found for customer ID: {}", id);
                return ApiResponseDto.statusNotFound("No data found");
            }
        } catch (DatabaseException e) {
            logger.error("Error occurred while fetching ride history for customer ID: {}. Error: {}",
                    id, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * Deletes the authenticated customer using the user ID from the JWT token.
     * </p>
     *
     * @return {@link ApiResponseDto}
     */
    @DeleteMapping("/me")
    public ApiResponseDto<?> deleteCustomerById() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Attempting to delete customer with ID: {}", id);
            userService.deleteById(id);
            logger.info("Successfully deleted customer with ID: {}", id);
            return ApiResponseDto.statusOk("Deleted successfully");
        } catch (DatabaseException e) {
            logger.error("Error occurred while deleting customer with ID: {}. Error: {}",
                    id, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method is responsible for fetching the profile of the currently authenticated customer.
     * </p>
     *
     * @return ResponseEntity<CustomerProfileDto>
     * The response entity containing the customer profile or an error message.
     */
    @GetMapping("/me/profiles")
    public ApiResponseDto<?> getCustomerProfile() {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            CustomerProfileDto customerProfile = customerService.getCustomerProfile(userId);
            logger.info("Fetched profile information for user with ID {}", userId);
            return ApiResponseDto.statusOk(customerProfile);
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found", userId);
            return ApiResponseDto.statusNotFound("Invalid customer ID");
        } catch (DatabaseException e) {
            logger.error("Error fetching profile information for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error occurred while fetching" +
                    " user profile", e);
        }
    }

    /**
     * <p>
     * This method is responsible for updating the tier of the currently authenticated customer.
     * </p>
     *
     * @return ResponseEntity<TierDto>
     * The response entity containing the updated tier or an error message.
     */
    @GetMapping("/me/tiers")
    public ApiResponseDto<?> updateAndGetCustomerTier() {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            TierDto tierdto = customerService.getCustomerTier(userId);
            logger.info("Updated tier for customer with ID {}", userId);
            return ApiResponseDto.statusOk(tierdto);
        } catch (NotFoundException e) {
            logger.warn("Customer with ID {} not found ", userId);
            return ApiResponseDto.statusNotFound("Invalid customer ID");
        } catch (DatabaseException e) {
            logger.error("Error updating tier for userId: {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error occurred while updating customer tier", e);
        }
    }

    /**
     * <p>
     * This method is responsible for updating the status of a ride.
     * </p>
     *
     * @param updateRideDto {@link UpdateRideDto}
     *                      The request body must contain all the fields of UpdateRideDto.
     * @return {@link ApiResponseDto}
     * The response entity containing the updated ride details or an error message.
     */
    @PutMapping("/me/rides")
    public ApiResponseDto<?> updateRideDetails(@RequestBody UpdateRideDto updateRideDto) {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            String customerId = customerService.retrieveCustomerIdByUserId(userId);
            UpdateRideResponseDto updateRideResponseDto = rideRequestService.updateRideDetails(customerId,
                    updateRideDto);
            logger.info("Updated ride details for ride with user ID {}", userId);
            return ApiResponseDto.statusOk(updateRideResponseDto);
        } catch (NotFoundException e) {
            logger.warn("user with ID {} not found", userId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (DatabaseException e) {
            logger.error("Error updating ride details for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error occurred while updating customer ride " +
                    "details", e);
        }
    }

    /**
     * <p>
     * This method handles the request to cancel a ride for the authenticated user.
     * </p>
     *
     * @return {@link ApiResponseDto}
     * The response entity containing the status of the cancelled ride or an error message.
     */
    @PatchMapping("/me")
    public ApiResponseDto<?> cancelRide() {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            String customerId = customerService.retrieveCustomerIdByUserId(userId);
            RideStatusDto rideStatusDto = rideRequestService.cancelRide(customerId);
            logger.info("Cancelled the ride successfully.");
            return ApiResponseDto.statusOk(rideStatusDto);
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found ", userId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (Exception e) {
            logger.error("Error updating ride details for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error occurred while updating" +
                    " customer ride details", e);
        }
    }
}