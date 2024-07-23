package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class DriverServiceImpl implements DriverService{
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    RideRequestService rideRequestService;
    @Autowired
    RideService rideService;
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    VehicleLocationService vehicleLocationService;

    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void updateDriverStatus(UpdateDriverStatusDto updateDriverStatusDto) {
        User user = userService.getUserByMobileNumber(updateDriverStatusDto.getPhoneNumber());
        Driver driver = driverRepository.findByUserId(user.getId());
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
    public List<RequestedRideDto> getRideRequests(GetRideRequestListsDto getRideRequestListsDto) {
        List<RequestedRideDto> requestedRideDtos = new ArrayList<>();
        List<RideRequest> rideRequests = rideRequestService.getAll();
        for(RideRequest rideRequest : rideRequests) {
            if(rideRequest.getPickupPoint().equalsIgnoreCase(getRideRequestListsDto.getLocation())
            && (rideRequest.getVehicleCategory().equalsIgnoreCase(getRideRequestListsDto.getCategory()))
            && (rideRequest.getStatus().equalsIgnoreCase("UnAssigned"))) {
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
        request.setStatus("Assigned");
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

    @Override
    public boolean updateDriverRating(int id, int ratings){
        Driver driver = driverRepository.findById(id).get();
        int currentRating = driver.getRatings();
        int updatedRating = (currentRating + ratings) / 2;
        driver.setRatings(updatedRating);
        Driver updatedDriver = driverRepository.save(driver);
        return !ObjectUtils.isEmpty(updatedDriver);
    }
}

