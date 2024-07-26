package com.i2i.zapcab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.repository.VehicleRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *     Implements {@link VehicleService}
 *     A service class which holds the vehicle details and performs some basic functionalities.
 * </p>
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public void saveVehicle(Vehicle vehicle) {

    }

    @Override
    @Transactional
    public void updateVehicleStatus(String status, Vehicle vehicle) {;
        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public void updateVehicleLocation(String Location, Vehicle vehicle) {

    }
}
