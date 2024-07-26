package com.i2i.zapcab.service;

import static com.i2i.zapcab.common.ZapCabConstant.ASSIGNED;
import static com.i2i.zapcab.common.ZapCabConstant.DRIVER_STATUS;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_DRIVER_STATUS;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_VEHICLE_STATUS;
import static com.i2i.zapcab.common.ZapCabConstant.PAYMENT_CASH;
import static com.i2i.zapcab.common.ZapCabConstant.RIDE_COMPLETED;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.helper.OTPService;
import com.i2i.zapcab.helper.RideRequestStatusEnum;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.DriverRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * Implements {@link DriverService}
 * <p>
 *     This class implements all the business logic that are related to the driver
 * </p>
 */
@Service
public class DriverServiceImpl implements DriverService {
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

    OTPService otpService = new OTPService();

    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Optional<Driver> driver = driverRepository.findById(user.get().getId());
        if(!driver.isPresent()) {
            Driver driver1 = driver.get();
            if (updateDriverStatusDto.getStatus().equalsIgnoreCase(DRIVER_STATUS)) {
                vehicleService.updateVehicleStatus("Available", driver1.getVehicle());
                vehicleLocationService.updateVehicleLocationByVehicleId(updateDriverStatusDto.getLocation(), driver1.getVehicle());
            } else if (updateDriverStatusDto.getStatus().equalsIgnoreCase(INITIAL_DRIVER_STATUS)
                    || updateDriverStatusDto.getStatus().equalsIgnoreCase("SUSPENDED")) {
                vehicleService.updateVehicleStatus("Un Available", driver1.getVehicle());
            }
            driver1.setStatus(updateDriverStatusDto.getStatus());
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
            int currentRating = driver.getRatings();
            int updatedRating = (currentRating + ratings) / 2;
            driver.setRatings(updatedRating);
            Driver updatedDriver = driverRepository.save(driver);
            return !ObjectUtils.isEmpty(updatedDriver);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while updating driver rating with its id :" + id, e);
        }
    }
    @Override
    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto) {
        try {
            List<RequestedRideDto> requestedRideDtos = new ArrayList<>();
            List<RideRequest> rideRequests = rideRequestService.getAll();
            for (RideRequest rideRequest : rideRequests) {
                if (rideRequest.getPickupPoint().equalsIgnoreCase(getRideRequestListsDto.getLocation())
                        && (rideRequest.getVehicleCategory().equalsIgnoreCase(getRideRequestListsDto.getCategory()))
                        && (rideRequest.getStatus().equalsIgnoreCase(String.valueOf(RideRequestStatusEnum.PENDING)))) {
                    RequestedRideDto requestedRideDto = RequestedRideDto.builder()
                            .rideId(rideRequest.getId())
                            .pickUpPoint(rideRequest.getPickupPoint())
                            .dropPoint(rideRequest.getDropPoint())
                            .distance(rideRequest.getDistance())
                            .customerName(rideRequest.getCustomer().getUser().getName())
                            .mobileNumber(rideRequest.getCustomer().getUser().getMobileNumber())
                            .build();
                    requestedRideDtos.add(requestedRideDto);
                }
            }
            return requestedRideDtos;
        } catch (Exception e) {
            throw new UnexpectedException("Unable to get the ride requests", e);
        }
    }

    @Override
    public synchronized RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto) {
        try {
            RideRequest request = rideRequestService.getRideByCustomerName(selectedRideDto);
            request.setStatus(String.valueOf(RideRequestStatusEnum.ASSIGNED));
            RideDetailsDto rideDetailsDto = RideDetailsDto.builder()
                    .customerName(request.getCustomer().getUser().getName())
                    .pickupPoint(request.getPickupPoint())
                    .distance(request.getDistance())
                    .mobileNumber(request.getCustomer().getUser().getMobileNumber())
                    .build();
            rideService.saveRide(request, getByMobileNumber(selectedRideDto.getMobileNumber()));
            request.setStatus(ASSIGNED);
            rideRequestService.updateRequest(request);
            return rideDetailsDto;
        } catch (Exception e) {
            throw new UnexpectedException("Unable to fetch the ride details", e);
        }
    }

    @Override
    public Driver getByMobileNumber(String mobileNumber) {
        try {
            return driverRepository.findDriverByMobileNumber(mobileNumber);
        } catch (Exception e) {
            throw new UnexpectedException("Error occurred while retrieving the driver detail", e);
        }
    }

    @Override
    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, MaskMobileNumberRequestDto
            maskMobileNumberRequestDto) {
        try {
            return userService.updateMaskMobileNumber(id, maskMobileNumberRequestDto.getIsMaskedMobileNumber());
        } catch (Exception e) {
            throw new UnexpectedException("Unable to masks the mobile number for the driver : "+ id, e);
        }
    }

    @Override
    public Boolean otpValidation(OtpRequestDto otpRequestDto) {
        try {
            User user = userService.getUserByMobileNumber(otpRequestDto.getCustomerMobileNumber());
            return otpService.validateOTP(user.getId(), otpRequestDto.getOtp());
        } catch (Exception e) {
             throw new UnexpectedException("Unable to verify the given otp", e);
        }
    }

    @Override
    public void updateDriverWallet(String id, String paymentMode, String rideStatus, int fare) {
        try {
            Optional<Driver> driver = driverRepository.findById(id);
            if (driver.isPresent()) {
                Driver drivers = driver.get();
                if (paymentMode.equalsIgnoreCase(PAYMENT_CASH) & rideStatus.equalsIgnoreCase(RIDE_COMPLETED)) {
                    int fareToReduce = Math.round((20 / 100) * fare);
                    if (drivers.getWallet() != 0) {
                        int updatedWallet = drivers.getWallet() - fareToReduce;
                        drivers.setWallet(updatedWallet);
                        driverRepository.save(drivers);
                    } else {
                        drivers.setStatus("Temporarily Unavailable");
                        drivers.getVehicle().setStatus(INITIAL_VEHICLE_STATUS);
                        driverRepository.save(drivers);
                    }
                }
            }
        } catch (Exception e) {
            throw new UnexpectedException("Unable to update the wallet for the driver : "+id, e);
        }
    }
}

