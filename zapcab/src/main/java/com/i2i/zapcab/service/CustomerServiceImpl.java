package com.i2i.zapcab.service;

import com.i2i.zapcab.common.FareCalculator;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.helper.OTPService;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VehicleLocationService vehicleLocationService;
    @Autowired
    private RideRequestService rideRequestService;
    @Autowired
    private RideService rideService;
    @Autowired
    private DriverService driverService;

    private final FareCalculator fareCalculator = new FareCalculator();

    private final OTPService otpService = new OTPService();

    @Override
    public VehicleAvailabilityResponseDto getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        logger.debug("Fetching available vehicles with location {} ",
                checkVehicleAvailabilityDto.getPickupPoint());
        VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = new VehicleAvailabilityResponseDto();
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        try {
            if (checkVehicleAvailabilityDto.getPickupPoint().
                    equalsIgnoreCase(checkVehicleAvailabilityDto.getDropPoint())) {
                logger.error("Same pickup and drop point : {}",
                        checkVehicleAvailabilityDto.getPickupPoint());
                return null;
            }
            vehicleLocationService.getVehiclesByLocation(
                            checkVehicleAvailabilityDto.getPickupPoint())
                    .forEach(vehicle -> {
                        RideRequestResponseDto rideRequestResponseDto = fareCalculator.calculateFare(
                                checkVehicleAvailabilityDto.getPickupPoint(),
                                checkVehicleAvailabilityDto.getDropPoint(),
                                vehicle.getVehicle().getCategory());
                        rideRequestResponseDtos.add(rideRequestResponseDto);
                    });
            vehicleAvailabilityResponseDto.setPickup(checkVehicleAvailabilityDto.getPickupPoint());
            vehicleAvailabilityResponseDto.setDrop(checkVehicleAvailabilityDto.getDropPoint());
            vehicleAvailabilityResponseDto.setRideRequestResponseDtos(rideRequestResponseDtos);
            logger.info("Fetched vehicles successfully for pickup point {}",
                    checkVehicleAvailabilityDto.getPickupPoint());
            return vehicleAvailabilityResponseDto;
        } catch (Exception e) {
            throw new DatabaseException("Error occurred while fetching vehicles" +
                    " and calculating fare", e);
        }
    }

    @Override
    public boolean saveRideRequest(String id, RideRequestDto rideRequestDto) {
        try {
            return rideRequestService.saveRideRequest(customerRepository.findByUserId(id), rideRequestDto);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while saving ride request", e);
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            logger.error("Un expected error happened while saving customer" +
                    customer.getUser().getName(), e);
            throw new DatabaseException("Un expected error happened while saving customer" +
                    customer.getUser().getName(), e);
        }
    }

    @Override
    public boolean updateRideAndDriverRating(String id, RideRatingDto ratings) {
        try {
            return driverService.updateDriverRating(rideService.updateRideRating(id, ratings),
                    ratings.getRatings());
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while Updating Ride And Driver Ratings", e);
        }
    }

    @Override
    public AssignedDriverDto getAssignedDriverDetails(String id) {
        try {
            RideRequest rideRequest = rideRequestService.checkStatusAssignedOrNot(id);
            Ride ride = rideService.getRideByRideRequest(rideRequest.getId());
            String otp = otpService.generateOTP(id);
            AssignedDriverDto assignedDriverDto = AssignedDriverDto.builder().Otp(otp).
                    name(ride.getDriver().getUser().getName()).
                    mobileNumber(ride.getDriver().getUser().getMobileNumber()).
                    ratings(ride.getDriver().getRatings()).
                    category(ride.getDriver().getVehicle().getCategory()).
                    model(ride.getDriver().getVehicle().getModel()).
                    licensePlate(ride.getDriver().getVehicle().getLicensePlate()).build();
            return !ObjectUtils.isEmpty(rideRequest) ? assignedDriverDto : null;
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while fetch assigned" +
                    " driver for the customer with id :" + id, e);
        }
    }

    @Override
    public CustomerProfileDto getCustomerProfile(String userId) {
        logger.debug("Fetching profile for user with ID: {}", userId);
        try {
            Customer customer = customerRepository.findByUserId(userId);
            if (null != customer) {
                logger.info("Successfully fetched profile for user with ID: {}", userId);
                return CustomerProfileDto.builder()
                        .name(customer.getUser().getName())
                        .email(customer.getUser().getEmail())
                        .mobileNumber(customer.getUser().getMobileNumber())
                        .gender(customer.getUser().getGender())
                        .tier(customer.getTier())
                        .build();
            } else {
                logger.warn("User profile not found for ID: {}", userId);
                throw new NotFoundException("User profile not found for ID: " + userId);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch user profile for ID: {}", userId, e);
            throw new DatabaseException("Failed to fetch user profile for ID: " + userId, e);
        }
    }

    @Override
    public void updateCustomerTier(String userId, TierDto tierDto) {
        logger.debug("Updating tier for customer with userId: {} to new tier: {}", userId, tierDto.getTier());
        try {
            Customer customer = customerRepository.findByUserId(userId);
            if (null != customer) {
                customer.setTier(tierDto.getTier());
                customerRepository.save(customer);
                logger.info("Successfully updated tier for customer with userId: {}", userId);
            } else {
                logger.warn("Customer not found for userId: {}", userId);
                throw new NotFoundException("Customer not found for userId: " + userId);
            }
        } catch (Exception e) {
            logger.error("Error updating tier for customer with userId: {}", userId, e);
            throw new DatabaseException("Error updating tier for customer with userId: " + userId, e);
        }
    }


    @Override
    public String retrieveCustomerIdByUserId(String userId) {
        try {
            Customer customer = customerRepository.findByUserId(userId);
            if (null == customer) {
                logger.warn("User ID not found: {}", userId);
                throw new NotFoundException("User ID not found " + userId);
            }
            logger.info("Successfully retrieved customer ID for user ID: {}", userId);
            return customer.getId();
        } catch (Exception e) {
            logger.error("Failed to retrieve the customer ID for user ID: {}", userId, e);
            throw new DatabaseException("Failed to retrieve the customer ID for user ID: " + userId, e);
        }
    }
}
