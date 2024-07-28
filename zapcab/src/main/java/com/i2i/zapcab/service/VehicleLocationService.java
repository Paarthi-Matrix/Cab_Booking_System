package com.i2i.zapcab.service;

import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * An interface that performs operations related to the location of the vehicle.
 * </p>
 */
@Component
public interface VehicleLocationService {
    /**
     * <p>
     * Retrieves a list of vehicle locations based on a given location.
     * </p>
     *
     * @param location The location to search for vehicles.
     * @return A list of VehicleLocation objects found at the specified location.
     * @throws DatabaseException if an error occurs while fetching the vehicle locations.
     */
    List<VehicleLocation> getVehiclesByLocation(String location);

    void saveVehicleLocation(VehicleLocation vehicleLocation);

    VehicleLocation getVehicleLocationById(int id);

    void updateVehicleLocationByVehicleId(String location, Vehicle vehicle);
}
