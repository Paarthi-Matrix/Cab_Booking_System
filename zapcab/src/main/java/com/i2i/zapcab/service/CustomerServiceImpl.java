package com.i2i.zapcab.service;

import com.i2i.zapcab.common.FareCalculator;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.OTPResponseDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.helper.OTPService;
import com.i2i.zapcab.mapper.RideRequestMapper;
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
        VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = new VehicleAvailabilityResponseDto();
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        try {
            if(checkVehicleAvailabilityDto.getPickupPoint().equalsIgnoreCase(checkVehicleAvailabilityDto.getDropPoint())){
                return null;
            }
            logger.info("Fetching vehicles for pickup point: {}",
                    checkVehicleAvailabilityDto.getPickupPoint());
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
            return vehicleAvailabilityResponseDto;
        } catch (Exception e) {
            logger.error("Error fetching available vehicles: {}", e.getMessage());
            throw new AuthenticationException("Error occurred while fetching vehicles and calculating fare",e);
        }
    }

    @Override
    public RideRequest saveRideRequest(String id, RideRequestDto rideRequestDto) {
        try {
           return rideRequestService.saveRideRequest(customerRepository.findByUserId(id), rideRequestDto);
        } catch (Exception e) {
            logger.error("Error Occurred while adding ride request {}" , e.getMessage());
            throw new UnexpectedException("Error Occurred while saving ride request", e);
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            logger.error("Un expected error happened while saving customer" +
                    customer.getUser().getName(), e);
            String errorMessage = "Un expected error happened while saving customer" +
                    customer.getUser().getName();
            throw new UnexpectedException(errorMessage, e);
        }
    }

    @Override
    public boolean updateRideAndDriverRating(String id, RideRatingDto ratings) {
        try {
            return driverService.updateDriverRating(rideService.updateRideRating(id, ratings),
                    ratings.getRatings());
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while Updating Ride And Driver Ratings", e);
        }
    }

    @Override
    public AssignedDriverDto getAssignedDriverDetails(String id){
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
        } catch(Exception e) {
            throw new UnexpectedException("Error Occurred while fetch assigned driver for the customer with id :"+id,e);
        }
    }

    /**
     * <p>
     *     Fetches the profile of a customer based on their user ID.
     * </p>
     * @param customerId
     *        The ID of the customer
     * @return {@link  CustomerProfileDto}
     *         The profile information of the customer.
     * @throws UnexpectedException
     *         If error occurs while retrieving the customer profile.
     */
    @Override
    public CustomerProfileDto getCustomerProfile(String customerId) {
        logger.debug("Fetching profile for customer with ID: {}", customerId);
        try {
            Customer customer = customerRepository.findByUserId(customerId);
            if (null != customer) {
                logger.info("Successfully fetched profile for customer with ID: {}", customerId);
                return CustomerProfileDto.builder()
                        .name(customer.getUser().getName())
                        .email(customer.getUser().getEmail())
                        .mobileNumber(customer.getUser().getMobileNumber())
                        .gender(customer.getUser().getGender())
                        .tier(customer.getTier())
                        .build();

            } else {
                logger.warn("Customer profile not found for ID: {}", customerId);
                throw new NotFoundException("Customer profile not found for ID: " + customerId);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch customer profile for ID: {}", customerId, e);
            throw new UnexpectedException("Failed to fetch customer profile for ID: " + customerId, e);
        }
    }

    /**
     * <p>
     *     Updates the tier of a customer based on the user ID.
     * </p>
     * @param userId
     *        The userId of the customer.
     * @param newTier
     *        The newTier to be set for the customer.
     * @throws UnexpectedException
     *         If error occurs while updating the customer tier.
     */
    @Override
    public void updateCustomerTier(String userId, String newTier) {
        logger.debug("Updating tier for customer with userId: {} to new tier: {}", userId, newTier);
        try {
            Customer customer = customerRepository.findByUserId(userId);
            if (null != customer) {
                customer.setTier(newTier);
                customerRepository.save(customer);
                logger.info("Successfully updated tier for customer with userId: {}", userId);
            } else {
                logger.warn("Customer not found for userId: {}", userId);
                throw new NotFoundException("Customer not found for userId: " + userId);
            }
        } catch (Exception e) {
            logger.error("Error updating tier for customer with userId: {}", userId, e);
            throw new UnexpectedException("Error updating tier for customer with userId: " + userId, e);
        }
    }
}
