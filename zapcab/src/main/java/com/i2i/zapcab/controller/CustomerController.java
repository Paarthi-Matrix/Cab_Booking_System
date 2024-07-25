package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.CustomerService;
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

    @GetMapping("me/vehicles")
    public ApiResponseDto<?> getAvailableVehiclesWithFare(
            @RequestBody CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        try {
            VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = customerService.
                    getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
            if (!ObjectUtils.isEmpty(vehicleAvailabilityResponseDto)) {
                return ApiResponseDto.statusOk(vehicleAvailabilityResponseDto);
            } else {
                return ApiResponseDto.statusOk("Same pickup and drop point not Allowed");
            }
        } catch (UnexpectedException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    @PostMapping("me/rideRequest")
    public ApiResponseDto<String> saveRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            if (!ObjectUtils.isEmpty(customerService.saveRideRequest(id, rideRequestDto))) {
                return ApiResponseDto.statusOk("Searching For Captain to Accept...");
            } else {
                return ApiResponseDto.statusNotFound(null);
            }
        } catch (UnexpectedException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    @PatchMapping("me/rides/{id}")
    public ApiResponseDto<String> updateRideAndDriverRating(@PathVariable int id, @RequestBody RideRatingDto ratings) {
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

    @GetMapping("me/driver")
    public ApiResponseDto<?> getAssignedDriverDetails() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            return ApiResponseDto.statusOk(customerService.getAssignedDriverDetails(id));
        } catch (UnexpectedException e) {
            logger.error(e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }
}