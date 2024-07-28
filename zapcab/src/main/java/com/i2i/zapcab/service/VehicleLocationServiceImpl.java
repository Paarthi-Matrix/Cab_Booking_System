package com.i2i.zapcab.service;

import com.i2i.zapcab.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.VehicleLocationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *     Implements {@link VehicleLocationService}
 *     A service class that manages the business logic to update the vehicle location frequently.
 * </p>
 */
@Service
public class VehicleLocationServiceImpl implements VehicleLocationService {

    @Autowired
    VehicleLocationRepository vehicleLocationRepository;

    @Override
    @Transactional
    public VehicleLocation getVehicleLocationById(int id) {
        return null;
    }

    @Override
    @Transactional
    public void updateVehicleLocationByVehicleId(String location, Vehicle vehicle) {
        VehicleLocation vehicleLocation = vehicleLocationRepository.findByVehicleId(vehicle.getId());
        vehicleLocation.setLocation(location);
        vehicleLocationRepository.save(vehicleLocation);
    }

    @Override
    public List<VehicleLocation> getVehiclesByLocation(String location) {
        try {
            return vehicleLocationRepository.findAllByLocation(location);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while fetching" +
                    " vehicles by its location: " + location, e);
        }
    }

    @Override
    public void saveVehicleLocation(VehicleLocation vehicleLocation) {
        vehicleLocationRepository.save(vehicleLocation);
    }
}
