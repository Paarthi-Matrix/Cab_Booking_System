package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

