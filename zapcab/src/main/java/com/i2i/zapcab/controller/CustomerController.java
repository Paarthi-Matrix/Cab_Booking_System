package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
     * @param checkVehicleAvailabilityDto Data transfer object containing the
     *                                    pickup and drop points.
     * @return A VehicleAvailabilityResponseDto containing the pickup and drop point and also containing the available vehicles with
     * their respective fares.
     */
    @GetMapping("me/vehicles")
    public ApiResponseDto<?> getAvailableVehiclesWithFare(
            @RequestBody CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        try {
            VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = customerService.
                    getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
            if (!ObjectUtils.isEmpty(vehicleAvailabilityResponseDto)) {
                return ApiResponseDto.statusOk(vehicleAvailabilityResponseDto);
            } else {
                return ApiResponseDto.statusBadRequest("Same pickup and drop point not Allowed");
            }
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
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
    @PostMapping("me/rideRequest")
    public ApiResponseDto<String> saveRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            if (customerService.saveRideRequest(id, rideRequestDto)) {
                return ApiResponseDto.statusOk("Searching For Captain to Accept...");
            } else {
                return ApiResponseDto.statusNotFound(null);
            }
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
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
            if (customerService.updateRideAndDriverRating(id, ratings)) {
                return ApiResponseDto.statusOk("Ratings successfully");
            } else {
                return ApiResponseDto.statusNotFound(null);
            }
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method handles the request to Retrieve the assigned driver details for a given ride request ID.
     * </p>
     *
     * @return An AssignedDriverDto object containing the driver's details
     * if the ride request is assigned, otherwise {@link ApiResponseDto}.
     */
    @GetMapping("me/driver")
    public ApiResponseDto<?> getAssignedDriverDetails() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            AssignedDriverDto assignedDriverDto = customerService.getAssignedDriverDetails(id);
            if (!ObjectUtils.isEmpty(assignedDriverDto)) {
                return ApiResponseDto.statusOk(assignedDriverDto);
            } else {
                return ApiResponseDto.statusAccepted("Searching for captain to accept request");
            }
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method handles the request to Retrieve all ride history records for a specific user by their ID.
     * Converts the ride history entities to DTOs before returning them.
     * </p>
     *
     * @return A list of RideHistoryResponseDto objects representing the user's ride history.
     */
    @GetMapping("me/rides")
    public ApiResponseDto<?> getAllRideHistory() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            List<RideHistoryResponseDto> rideHistoryResponseDtos = customerService.getAllRideHistoryById(id);
            if (!rideHistoryResponseDtos.isEmpty()) {
                return ApiResponseDto.statusOk(rideHistoryResponseDtos);
            } else {
                return ApiResponseDto.statusNotFound("No data Found");
            }
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
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
    @DeleteMapping("me")
    public ApiResponseDto<?> deleteCustomerById() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            userService.deleteById(id);
            return ApiResponseDto.statusOk("Deleted successfully");
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for fetching the profile of the currently authenticated customer.
     * </p>
     *
     * @return ResponseEntity<CustomerProfileDto>
     *         The response entity containing the customer profile or an error message.
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
            return ApiResponseDto.statusInternalServerError("Error occurred while fetching user profile", e);
        }
    }

    /**
     * <p>
     *     This method is responsible for updating the tier of the currently authenticated customer.
     * </p>
     *
     * @return ResponseEntity<TierDto>
     *         The response entity containing the updated tier or an error message.
     */
    @GetMapping("/me/tiers")
    public ApiResponseDto<?> getCustomerTier() {
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
     *     This method is responsible for updating the status of a ride.
     * </p>
     *
     * @param updateRideDto {@link UpdateRideDto}
     *        The request body must contain all the fields of UpdateRideDto.
     * @return ResponseEntity<UpdateResponseDto>
     *         The response entity containing the updated ride details or an error message.
     */
    @PutMapping("/me/rides")
    public ApiResponseDto<?> updateRideDetails(@RequestBody UpdateRideDto updateRideDto) {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            String customerId = customerService.retrieveCustomerIdByUserId(userId);
            UpdateRideResponseDto updateRideResponseDto = rideRequestService.updateRideDetails(customerId, updateRideDto);
            logger.info("Updated ride details for ride with user ID {}", userId);
            return ApiResponseDto.statusOk(updateRideResponseDto);
        } catch (NotFoundException e) {
            logger.warn("user with ID {} not found", userId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (DatabaseException e) {
            logger.error("Error updating ride details for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error occurred while updating customer ride details", e);
        }
    }
}