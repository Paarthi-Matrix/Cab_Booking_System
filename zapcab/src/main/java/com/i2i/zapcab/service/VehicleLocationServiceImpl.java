package com.i2i.zapcab.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.VehicleLocationRepository;

/**
 * <p>
 * Service implementation for managing vehicle locations.
 * This class handles operations related to vehicle locations, such as retrieving,
 * updating, and fetching vehicle locations by location.
 * {@link VehicleLocationService}
 * </p>
 */
@Service
public class VehicleLocationServiceImpl implements VehicleLocationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    VehicleLocationRepository vehicleLocationRepository;

    @Override
    @Transactional
    public VehicleLocation getVehicleLocationById(int id) { //TODO: Implementation pending for future updates
        return null;
    }

    @Override
    @Transactional
    public void updateVehicleLocationByVehicleId(String location, Vehicle vehicle) {
        VehicleLocation vehicleLocation = vehicleLocationRepository.findByVehicleId(vehicle.getId());
        vehicleLocation.setLocation(location);
        try {
            logger.debug("Saving the updated vehicle location...");
            vehicleLocationRepository.save(vehicleLocation);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating the vehicle " +
                    vehicle.getLicensePlate() + " with location " + location, e);
            throw new DatabaseException("Unexpected error occurred while updating the vehicle " +
                    vehicle.getLicensePlate() + " with location " + location, e);
        }
    }

    @Override
    @Transactional
    public List<VehicleLocation> getVehiclesByLocation(String location) {
        try {
            return vehicleLocationRepository.findAllByLocation(location);
        } catch (Exception e) {
            logger.error("Error Occurred while fetching vehicles by its location: {}", location, e);
            throw new DatabaseException("Error Occurred while fetching" +
                    " vehicles by its location: " + location, e);
        }
    }

    @Override
    @Transactional
    public void saveVehicleLocation(VehicleLocation vehicleLocation) {
        try {
            vehicleLocationRepository.save(vehicleLocation);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving vehicle {}," +
                    " to the location {}", vehicleLocation.getVehicle().getLicensePlate(), vehicleLocation.getLocation(), e);
            String errorMessage = "Unexpected error occurred while saving vehicle {}," + vehicleLocation.getVehicle().getLicensePlate() +
                    " to the location " + vehicleLocation.getLocation();
            throw new DatabaseException(errorMessage, e);
        }
    }
}