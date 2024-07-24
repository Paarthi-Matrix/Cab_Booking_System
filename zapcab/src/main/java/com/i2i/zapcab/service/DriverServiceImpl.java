package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
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
public class DriverServiceImpl implements DriverService{
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    VehicleLocationService vehicleLocationService;
    @Autowired
    RideRequestService rideRequestService;
    @Autowired
    RideService rideService;

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
                   || updateDriverStatusDto.getStatus().equalsIgnoreCase("SUSPENDED")){
            vehicleService.updateVehicleStatus("Un Available", driver.getVehicle());
        }
        driver.setStatus(updateDriverStatusDto.getStatus());
    }

    @Override
    public void changePassword(String id, String newPassword) {
        userService.changePassword(id, newPassword);
    }

    @Override
    public boolean updateDriverRating(int id, int ratings){
        Driver driver = driverRepository.findById(id).get();
        int currentRating = driver.getRatings();
        int updatedRating = (currentRating + ratings) / 2;
        driver.setRatings(updatedRating);
        Driver updatedDriver = driverRepository.save(driver);
        return !ObjectUtils.isEmpty(updatedDriver);
    }
    @Override
    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto) {
        List<RequestedRideDto> requestedRideDtos = new ArrayList<>();
        List<RideRequest> rideRequests = rideRequestService.getAll();
        for(RideRequest rideRequest : rideRequests) {
            if(rideRequest.getPickupPoint().equalsIgnoreCase(getRideRequestListsDto.getLocation())
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
    }

    @Override
    public synchronized RideDetailsDto getRideDetails(DriverSelectedRideDto selectedRideDto) {//To-Do
        RideRequest request = rideRequestService.getRideByCustomerName(selectedRideDto);
        request.setStatus(String.valueOf(RideRequestStatusEnum.ASSIGNED));
        RideDetailsDto rideDetailsDto  = RideDetailsDto.builder()
                .customerName(request.getCustomer().getUser().getName())
                .pickupPoint(request.getPickupPoint())
                .distance(request.getDistance())
                .mobileNumber(request.getCustomer().getUser().getMobileNumber())
                .build();
        rideService.saveRide(request, selectedRideDto.getMobileNumber(), getByMobileNumber(selectedRideDto.getMobileNumber()));
        return rideDetailsDto;
    }

    @Override
    public Driver getByMobileNumber(String mobileNumber) {
        return driverRepository.findDriverByMobileNumber(mobileNumber);
    }
}

