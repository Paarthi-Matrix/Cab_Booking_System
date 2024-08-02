package com.i2i.zapcab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.i2i.zapcab.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.i2i.zapcab.dto.CancelRideRequestDto;
import com.i2i.zapcab.dto.CancelRideResponseDto;
import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.RideStatusDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.RideRequestStatusEnum;
import com.i2i.zapcab.mapper.RideRequestMapper;
import com.i2i.zapcab.repository.DriverRepository;

import static com.i2i.zapcab.common.ZapCabConstant.*;

/**
 * Implements {@link DriverService}
 * <p>
 * This class implements all the business logic that are related to the driver
 * </p>
 */
@Service
public class DriverServiceImpl implements DriverService {
    private final static Logger logger = LogManager.getLogger(DriverServiceImpl.class);
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleLocationService vehicleLocationService;
    @Autowired
    private RideRequestService rideRequestService;
    @Autowired
    private RideService rideService;
    @Autowired
    private OtpServiceImpl otpService;

    private final RideRequestMapper rideRequestMapper = new RideRequestMapper();

    @Override
    public Driver saveDriver(Driver driver) {
        try {
            logger.info("Saving the driver details..");
            Driver driver1 =  driverRepository.save(driver);
            logger.info("Driver details saved {}", driver1.getId());
            return driver1;
        } catch (Exception e) {
            logger.error("Unable to save the driver {}", driver.getUser().getName());
            throw new DatabaseException("Error occurred while saving the driver "
                    + driver.getUser().getName(), e);
        }
    }

    @Override
    public void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Driver driver = driverRepository.findByUserId(id);
        if (!ObjectUtils.isEmpty(driver)) {
            if (updateDriverStatusDto.getStatus().equalsIgnoreCase(DRIVER_STATUS)) {
                vehicleService.updateVehicleStatus("Available", driver.getVehicle());
                vehicleLocationService.updateVehicleLocationByVehicleId(updateDriverStatusDto.getLocation(), driver.getVehicle().getId());
            } else if (updateDriverStatusDto.getStatus().equalsIgnoreCase(INITIAL_DRIVER_STATUS)
                    || updateDriverStatusDto.getStatus().equalsIgnoreCase("SUSPENDED")) {
                vehicleService.updateVehicleStatus("Un Available", driver.getVehicle());
            }
            driver.setStatus(updateDriverStatusDto.getStatus());
            driverRepository.save(driver);
        }
    }

    @Override
    public void changePassword(String id, String newPassword) {
        userService.changePassword(id, newPassword);
    }

    @Override
    public boolean updateDriverRating(String id, int ratings) {
        try {
            Driver driver = driverRepository.findById(id).get();
            double currentRating = driver.getRatings();
            double updatedRating = (currentRating + ratings) / 2;
            driver.setRatings(updatedRating);
            return !ObjectUtils.isEmpty(driverRepository.save(driver));
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while updating driver rating with its id :" + id, e);
        }
    }

    @Override
    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto) {
        try {
            logger.info("Fetching the ride request details from the location {} ", getRideRequestListsDto.getLocation());
            List<RequestedRideDto> requestedRideDtos = new ArrayList<>();
            List<RideRequest> rideRequests = rideRequestService.getAll();
            for (RideRequest rideRequest : rideRequests) {
                if (rideRequest.getPickupPoint().equalsIgnoreCase(getRideRequestListsDto.getLocation())
                        && (rideRequest.getVehicleCategory().equalsIgnoreCase(getRideRequestListsDto.getCategory()))
                        && (rideRequest.getStatus().equalsIgnoreCase(String.valueOf(RideRequestStatusEnum.PENDING)))) {
                    RequestedRideDto requestedRideDto = rideRequestMapper.entityToDto(rideRequest);
                    requestedRideDtos.add(requestedRideDto);
                }
            }
            return requestedRideDtos;
        } catch (Exception e) {
            logger.error(" Error occurred while getting the list of requests for the category {} and location {}",
                    getRideRequestListsDto.getCategory(), getRideRequestListsDto.getLocation());
            throw new DatabaseException("Unable to get the ride requests", e);
        }
    }

    @Override
    public synchronized RideDetailsDto acceptRide(DriverSelectedRideDto selectedRideDto, String id) {
        try {
            logger.info("Fetching the ride details that have been accepted by the driver");
            if (ObjectUtils.isEmpty(selectedRideDto)) {
                logger.warn("Invalid details provided: {}", selectedRideDto);
                throw new IllegalArgumentException("Invalid ride selection details provided");
            }
            RideRequest request = rideRequestService.getRideByCustomerName(selectedRideDto);
            Driver getDriver = driverRepository.findByUserId(id);
            if (null == request) {
                logger.warn("No ride request found for customer: {}", selectedRideDto.getCustomerName());
                throw new NotFoundException("Ride request not found for customer: " + selectedRideDto.getCustomerName());
            }
            vehicleService.updateVehicleStatus(VEHICLE_STATUS_UNAVAILABLE, getDriver.getVehicle());
            request.setStatus(ASSIGNED);
            RideDetailsDto rideDetailsDto = RideDetailsDto.builder()
                    .customerName(request.getCustomer().getUser().getName())
                    .pickupPoint(request.getPickupPoint())
                    .distance(request.getDistance())
                    .mobileNumber(request.getCustomer().getUser().getMobileNumber())
                    .build();
            rideService.saveRide(request, getDriver);
            logger.info("Updating the request status to ASSIGNED");
            rideRequestService.updateRequest(request);
            logger.info("Ride details successfully retrieved and updated for customer: {}", selectedRideDto.getCustomerName());
            return rideDetailsDto;
        } catch (IllegalArgumentException | NotFoundException e) {
            logger.error("Validation error: {}", e.getMessage());
            throw new DatabaseException("Error occurred while validating the details", e);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving the ride details of the customer: {}", selectedRideDto.getCustomerName(), e);
            throw new DatabaseException("Unable to fetch the ride details", e);
        }
    }

    @Override
    public Driver getByMobileNumber(String mobileNumber) {
        try {
            logger.info("Finding the driver details by giving mobile number {} ", mobileNumber);
            return driverRepository.findDriverByMobileNumber(mobileNumber);
        } catch (Exception e) {
            logger.error("Unable to get the driver details");
            throw new DatabaseException("Error occurred while retrieving the driver detail", e);
        }
    }

    @Override
    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, MaskMobileNumberRequestDto maskMobileNumberRequestDto) {
        try {
            logger.info("Updating masked mobile number for user ID: {}", id);
            MaskMobileNumberResponseDto response = userService.updateMaskMobileNumber(id,
                    maskMobileNumberRequestDto.getIsMaskedMobileNumber());
            logger.info("Masked mobile number updated successfully for user ID: {}", id);
            return response;
        } catch (Exception e) {
            logger.error("Unable to mask the mobile number for user ID: {}", id, e);
            throw new DatabaseException("Unable to mask the mobile number for the user: " + id, e);
        }
    }

    @Override
    public Boolean otpValidation(OtpRequestDto otpRequestDto, String id) {
        try {
            logger.info("Starting OTP validation for mobile number: {}", otpRequestDto.getCustomerMobileNumber());
            User user = userService.getUserByMobileNumber(otpRequestDto.getCustomerMobileNumber());
            boolean isValid = otpService.validateOTP(user.getId(), otpRequestDto.getOtp());
            if (isValid) {
                rideService.updateRideStatus(id, RideStatusDto.builder().status(RIDE_STARTED).build());
            }
            logger.info("OTP validation result for mobile number {}: {}", otpRequestDto.getCustomerMobileNumber(), isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error occurred during OTP validation for mobile number: {}", otpRequestDto.getCustomerMobileNumber(), e);
            throw new DatabaseException("Unable to verify the given otp", e);
        }
    }

    @Override
    public void updateDriverWallet(String id, String paymentMode, String rideStatus, double fare) {
        try {
            logger.info("Updating driver wallet for driver ID: {}", id);
            Optional<Driver> driver = driverRepository.findById(id);
            if (driver.isPresent()) {
                Driver drivers = driver.get();
                logger.info("Driver found: {}", drivers);
                if (paymentMode.equalsIgnoreCase(PAYMENT_CASH) && rideStatus.equalsIgnoreCase(RIDE_COMPLETED)) {
                    double fareToReduce = Math.round(ZAPCAB_RIDE_COMMISSION_PERCENTAGE * fare); // Corrected division to get a float value
                    logger.info("Fare to reduce: {}", fareToReduce);
                    if (drivers.getWallet() > 0) {
                        double updatedWallet = drivers.getWallet() - fareToReduce;
                        drivers.setWallet(updatedWallet);
                        logger.info("Updated wallet amount: {}", updatedWallet);
                        driverRepository.save(drivers);
                        logger.info("Driver wallet updated successfully");
                    } else {
                        drivers.setStatus("Temporarily Unavailable");
                        drivers.getVehicle().setStatus(VEHICLE_STATUS_UNAVAILABLE);
                        driverRepository.save(drivers);
                        logger.info("Driver set to temporarily unavailable due to zero wallet balance");
                    }
                } else {
                    logger.info("Payment mode or ride status did not match the required criteria");
                }
            } else {
                logger.warn("Driver with ID: {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Unable to update the wallet for the driver: {}", id, e);
            throw new DatabaseException("Unable to update the wallet for the driver: " + id, e);
        }
    }



    @Override
    public String retrieveDriverIdByUserId(String userId) {
        try {
            Driver driver = driverRepository.findByUserId(userId);
            if (null == driver) {
                logger.warn("Driver not found for user ID: {}", userId);
                throw new NotFoundException("Driver not found for user ID: " + userId);
            }
            logger.info("Successfully retrieved driver ID for user ID: {}", userId);
            return driver.getId();
        } catch (Exception e) {
            logger.error("Failed to retrieve the driver ID for user ID: {}", userId, e);
            throw new DatabaseException("Failed to retrieve the driver ID for user ID: " + userId, e);
        }
    }

    @Override
    public CancelRideResponseDto cancelRide(CancelRideRequestDto cancelRideRequestDto, String id) {
        Optional<Ride> getRide = rideService.getRideById(cancelRideRequestDto.getRideId());
        Driver driver = driverRepository.findByUserId(id);
        try {
            if (!getRide.isPresent()) {
                logger.error("No ride found for the id {}", cancelRideRequestDto.getRideId());
            }
            Ride ride = getRide.get();
            if (driver.getNoOfCancellation() == 0) {
                driver.setStatus(TEMPORARILY_SUSPENDED);
            }
            ride.setStatus("cancel");
            driver.setNoOfCancellation(driver.getNoOfCancellation() - 1);
            return CancelRideResponseDto.builder().message("You have only one cancellation more !!").build();
        } catch (Exception e) {
            throw new DatabaseException("Unable to cancel the ride : "+cancelRideRequestDto.getRideId(), e);
        }
    }

    @Override
    public Optional<Driver> getDriverById(String driverId) {
        try {
            return driverRepository.findById(driverId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching the driver by id {} ", driverId) ;
            throw new DatabaseException("Error occurred while fetching the driver by id " +
            driverId, e);
        }
    }

    @Override
    public String getVehicleIdByDriverId(String driverId) {
        try {
            Optional<Driver> driver = driverRepository.findById(driverId);
            return driver.get().getVehicle().getId();
        } catch (Exception e) {
            logger.error("Error occurred while fetching the driver by id {} ", driverId) ;
            throw new DatabaseException("Error occurred while fetching the driver by id " +
                    driverId, e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDriverStatusAndCancellation() {
        driverRepository.findDriversByStatusAndCancellation()
                .forEach(driver -> {
                    driver.setStatus(INITIAL_DRIVER_STATUS);
                    driver.setNoOfCancellation(2);
                });
    }
}