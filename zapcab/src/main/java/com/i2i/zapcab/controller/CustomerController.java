package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideInvoiceDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.dto.UpdateResponseDto;
import com.i2i.zapcab.dto.UpdateRideDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.CustomerService;
import com.i2i.zapcab.service.HistoryService;
import com.i2i.zapcab.service.HistoryService;
import com.i2i.zapcab.service.RideRequestService;
import com.i2i.zapcab.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private HistoryService historyService;
    @Autowired
    private RideService rideService;
    @Autowired
    private RideRequestService rideRequestService;

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
        } catch (UnexpectedException e) {
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
        } catch (UnexpectedException e) {
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
        } catch (UnexpectedException e) {
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
            if (ObjectUtils.isEmpty(assignedDriverDto)) {
                return ApiResponseDto.statusOk(assignedDriverDto);
            } else {
                return ApiResponseDto.statusAccepted("Searching for captain to accept request");
            }
        } catch (UnexpectedException e) {
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
            List<RideHistoryResponseDto> rideHistoryResponseDtos = historyService.getAllRideHistoryById(id);
            if (!rideHistoryResponseDtos.isEmpty()) {
                return ApiResponseDto.statusOk(rideHistoryResponseDtos);
            } else {
                return ApiResponseDto.statusNotFound("No data Found");
            }
        } catch (UnexpectedException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for fetching the profile of the currently authenticated customer.
     * </p>
     * @return ResponseEntity<CustomerProfileDto>
     *         The response entity containing the customer profile or an error message.
     */
    @GetMapping("/me/profiles")
    public ApiResponseDto<?> getCustomerProfile() {
        String customerId = JwtDecoder.extractUserIdFromToken();
        try {
            CustomerProfileDto customerProfile = customerService.getCustomerProfile(customerId);
            logger.info("Fetched profile information for customer with ID {}", customerId);
            return ApiResponseDto.statusOk(customerProfile);
        } catch (NotFoundException e) {
            logger.warn("Customer with ID {} not found", customerId);
            return ApiResponseDto.statusNotFound("Invalid customer ID");
        } catch (UnexpectedException e) {
            logger.error("Error fetching profile information for customer with ID {}", customerId, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for updating the tier of the currently authenticated customer.
     * </p>
     * @return ResponseEntity<TierDto>
     *         The response entity containing the updated tier or an error message.
     */
    @PutMapping("/me/tiers")
    public ApiResponseDto<?> updateUserTier() {
        String userId = JwtDecoder.extractUserIdFromToken();
        System.out.println(userId);
        try {
            TierDto tierdto = historyService.updateCustomerTier(userId);
            logger.info("Updated tier for customer with ID {}", userId);
            return ApiResponseDto.statusOk(tierdto);
        } catch (NotFoundException e) {
            logger.warn("Customer with ID {} not found ", userId);
            return ApiResponseDto.statusNotFound("Invalid customer ID");
        } catch (UnexpectedException e) {
            logger.error("Error updating tier for userId: {}", userId, e);
            return ApiResponseDto.statusInternalServerError(new TierDto("Error: " + e.getMessage()), e);
        }
    }

    /**
     * <p>
     *     This method is responsible for updating the status of a ride.
     * </p>
     * @param id {@link int}
     *        The ID of the ride to be updated.
     * @param statusDto {@link StatusDto}
     *        The request body must contain all the fields of StatusDto.
     * @return ResponseEntity<RideResponseDto>
     *         The response entity containing the updated ride status or an error message.
     */
    @PatchMapping("/{id}/status")
    public ApiResponseDto<?> updateRideStatus(@PathVariable String id, @RequestBody StatusDto statusDto) {
        try {
            RideResponseDto rideResponseDto = rideService.updateRideStatus(id, statusDto);
            logger.info("Updated ride status for ride with ID {}", id);
            return ApiResponseDto.statusOk(rideResponseDto);
        } catch (NotFoundException e) {
            logger.warn("Ride with ID {} not found", id);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (UnexpectedException e) {
            logger.error("Error updating ride status for ride with ID {}", id, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for generating an invoice for a ride.
     * </p>
     * @param rideId
     *        The ID of the ride for which the invoice is to be generated.
     * @return ResponseEntity<RideInvoiceDto>
     *         The response entity containing the ride invoice or an error message.
     */
    @GetMapping("/{rideId}/invoices")
    public ApiResponseDto<?> getRideInvoice(@PathVariable String rideId) {
        try {
            RideInvoiceDto rideInvoiceDto = rideService.generateRideInvoice(rideId);
            logger.info("Generated invoice for ride ID {}", rideId);
            return ApiResponseDto.statusOk(rideInvoiceDto);
        } catch (NotFoundException e) {
            logger.warn("Ride with ID {} not found ", rideId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (UnexpectedException e) {
            logger.error("Error generating invoice for ride ID {}", rideId, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for tracking the status of a ride.
     * </p>
     * @param id
     *        The ID of the ride to be tracked.
     * @return ResponseEntity<RideResponseDto>
     *         The response entity containing the ride status or an error message.
     */
    @GetMapping("/{id}")
    public ApiResponseDto<?> trackRideStatus(@PathVariable String id) {
        try {
            RideResponseDto rideResponseDto = rideService.trackRideStatus(id);
            logger.info("Tracked ride status for ride with ID {}", id);
            return ApiResponseDto.statusOk(rideResponseDto);
        } catch (NotFoundException e) {
            logger.warn("Ride with ID {} not found: ", id);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (UnexpectedException e) {
            logger.error("Error tracking ride status for ride with ID {}", id, e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     *     This method is responsible for updating the details of a ride.
     * </p>
     * @param id
     *        The ID of the ride to be updated.
     * @param updateRideDto {@link UpdateRideDto}
     *        The request body must contain all the fields of UpdateRideDto.
     * @return ResponseEntity<UpdateResponseDto>
     *         The response entity containing the updated ride details or an error message.
     */
    @PutMapping("/{id}/rides")
    public ResponseEntity<?> updateRideDetails(@PathVariable String id, @RequestBody UpdateRideDto updateRideDto) {
        try {
            UpdateResponseDto updateResponseDto = rideRequestService.updateRideDetails(id, updateRideDto);
            logger.info("Updated ride details for ride with ID {}", id);
            return ResponseEntity.ok(updateResponseDto);
        } catch (NotFoundException e) {
            logger.warn("Ride with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid ID");
        } catch (UnexpectedException e) {
            logger.error("Error updating ride details for ride with ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}