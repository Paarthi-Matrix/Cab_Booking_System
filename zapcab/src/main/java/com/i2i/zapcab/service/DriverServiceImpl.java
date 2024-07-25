package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.ChangePasswordRequestDto;
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
import com.i2i.zapcab.exception.UnexpectedException;
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
        Driver driver = driverRepository.findByUserId(user.get().getId());
        if (updateDriverStatusDto.getStatus().equalsIgnoreCase("ONDUTY")) {
            vehicleService.updateVehicleStatus("Available", driver.getVehicle());
            vehicleLocationService.updateVehicleLocationByVehicleId(updateDriverStatusDto.getLocation(), driver.getVehicle());
        } else if (updateDriverStatusDto.getStatus().equalsIgnoreCase("OFFDUTY")
                || updateDriverStatusDto.getStatus().equalsIgnoreCase("SUSPENDED")) {
            vehicleService.updateVehicleStatus("Un Available", driver.getVehicle());
        }
        driver.setStatus(updateDriverStatusDto.getStatus());
    }

    @Override
    public void changePassword(String id, String newPassword) {
        userService.changePassword(id, newPassword);

    }

    @Override
    public boolean updateDriverRating(int id, int ratings) {
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
            request.setStatus("Assigned");
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
    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, MaskMobileNumberRequestDto maskMobileNumberRequestDto) {
        return userService.updateMaskMobileNumber(id, maskMobileNumberRequestDto.getIsMaskedMobileNumber());
    }

    @Override
    public Boolean otpValidation(OtpRequestDto otpRequestDto) {
        try {
            User user = userService.getUserByMobileNumber(otpRequestDto.getCustomerMobileNumber());
            return otpService.validateOTP(user.getId(), otpRequestDto.getOtp());
        } catch (Exception e) {
            return false;
        }
    }
}

