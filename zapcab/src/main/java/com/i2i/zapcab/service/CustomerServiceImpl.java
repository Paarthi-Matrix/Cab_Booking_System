package com.i2i.zapcab.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.CustomerRepository;


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
    @Autowired
    private HistoryService historyService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private FareCalculatorService fareCalculatorService;

    @Override
    public VehicleAvailabilityResponseDto getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        logger.debug("Fetching available vehicles with location {} ",
                checkVehicleAvailabilityDto.getPickupPoint());
        VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = new VehicleAvailabilityResponseDto();
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        try {
            if (checkVehicleAvailabilityDto.getPickupPoint().toUpperCase().
                    equalsIgnoreCase(checkVehicleAvailabilityDto.getDropPoint().toUpperCase())) {
                logger.error("Same pickup and drop point : {}",
                        checkVehicleAvailabilityDto.getPickupPoint());
                return null;
            }
            logger.info("Fetching vehicles for pickup point: {}",
                    checkVehicleAvailabilityDto.getPickupPoint().toUpperCase());
            vehicleLocationService.getVehiclesByLocation(
                            checkVehicleAvailabilityDto.getPickupPoint().toUpperCase())
                    .forEach(vehicle -> {
                        RideRequestResponseDto rideRequestResponseDto = fareCalculatorService.calculateFare(
                                checkVehicleAvailabilityDto.getPickupPoint().toUpperCase(),
                                checkVehicleAvailabilityDto.getDropPoint().toUpperCase(),
                                vehicle.getVehicle().getCategory());
                        rideRequestResponseDtos.add(rideRequestResponseDto);
                    });
            vehicleAvailabilityResponseDto.setPickup(checkVehicleAvailabilityDto.getPickupPoint().toUpperCase());
            vehicleAvailabilityResponseDto.setDrop(checkVehicleAvailabilityDto.getDropPoint().toUpperCase());
            vehicleAvailabilityResponseDto.setRideRequestResponseDtos(rideRequestResponseDtos);
            logger.info("Fetched vehicles successfully for pickup point {}",
                    checkVehicleAvailabilityDto.getPickupPoint().toUpperCase());
            return vehicleAvailabilityResponseDto;
        } catch (Exception e) {
            throw new DatabaseException("Error occurred while fetching vehicles" +
                    " and calculating fare", e);
        }
    }

    @Override
    public boolean saveRideRequest(String id, RideRequestDto rideRequestDto) {
        try {
            logger.info("Attempting to save ride request for user ID: {}", id);
            boolean result = rideRequestService.saveRideRequest(customerRepository.findByUserId(id), rideRequestDto);
            logger.info("Ride request saved successfully for user ID: {}", id);
            return result;
        } catch (Exception e) {
            logger.error("Error Occurred while adding ride request {}", e.getMessage());
            throw new DatabaseException("Error Occurred while saving ride request", e);
        }
    }

    /**
     * <p>
     * This method is used to save the customer to the repository.
     * </p>
     *
     * @param customer {@link Customer}
     * @throws DatabaseException {@link DatabaseException}
     *                           Thrown while saving customer entity to the repository.
     */
    @Override
    public void saveCustomer(Customer customer) {
        try {
            logger.info("Attempting to save customer: {}", customer.getUser().getName());
            customerRepository.save(customer);
            logger.info("Customer saved successfully: {}", customer.getUser().getName());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving customer: {}. Error: {}", customer.getUser().getName(), e.getMessage(), e);
            throw new DatabaseException("Unexpected error occurred while saving customer: " + customer.getUser().getName(), e);
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
            logger.info("Fetching assigned driver details for customer ID: {}", id);
            RideRequest rideRequest = rideRequestService.checkStatusAssignedOrNot(id);
            if (rideRequest == null) {
                logger.info("No ride request found for customer ID: {}", id);
                return null;
            }
            Ride ride = rideService.getRideByRideRequest(rideRequest.getId());
            if (ride == null) {
                logger.info("No ride found for ride request ID: {}", rideRequest.getId());
                return null;
            }
            String otp = otpService.generateOTP(id);
            AssignedDriverDto assignedDriverDto = AssignedDriverDto.builder()
                    .Otp(otp)
                    .name(ride.getDriver().getUser().getName())
                    .mobileNumber(ride.getDriver().getUser().getMobileNumber())
                    .ratings(ride.getDriver().getRatings())
                    .category(ride.getDriver().getVehicle().getCategory())
                    .model(ride.getDriver().getVehicle().getModel())
                    .licensePlate(ride.getDriver().getVehicle().getLicensePlate())
                    .build();

            logger.info("Assigned driver details retrieved successfully for customer ID: {}", id);
            return assignedDriverDto;
        } catch (Exception e) {
            logger.error("Error occurred while fetching assigned driver details for customer ID: {}. Error: {}", id, e.getMessage(), e);
            throw new DatabaseException("Error occurred while fetching assigned driver for the customer with ID: " + id, e);
        }
    }

    @Override
    public List<RideHistoryResponseDto> getAllRideHistoryById(String id) {
        return historyService.getAllRideHistoryById(id);
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

    @Override
    public TierDto getCustomerTier(String userId) {
        try {
            logger.info("Attempting to get tier for the customer {}", userId);
            TierDto tierDto = historyService.getCustomerTier(userId);
            logger.info("Updating the customer {} tier", userId);
            updateCustomerTier(userId, tierDto);
            return tierDto;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get or update the customer tier");
        }
    }
}