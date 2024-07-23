package com.i2i.zapcab.service;

import com.i2i.zapcab.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.Vehicle;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Override
    public void saveVehicle(Vehicle vehicle) {

    }

    @Override
    public void updateVehicleStatus(String status, Vehicle vehicle) {
        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
    }

    @Override
    public void updateVehicleLocation(String Location, Vehicle vehicle) {
    }
}
