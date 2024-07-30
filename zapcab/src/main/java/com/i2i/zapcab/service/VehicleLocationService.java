package com.i2i.zapcab.service;

import java.util.List;

import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Service interface for managing vehicle locations.
 * This interface defines the contract for operations related to vehicle locations,
 * such as retrieving vehicles by location, getting a vehicle location by its ID,
 * and updating the location of a vehicle.
 * </p>
 */

@Component
public interface VehicleLocationService {

    /**
     * <p>
     * This method is used to fetch the list of vehicles in particular location.
     * </p>
     *
     * @param location The location to search for vehicles.
     * @return List<VehicleLocation>.
     * @throws DatabaseException {@link  DatabaseException}
     *                           Arises when there is read anomalies in Vehicle Location database.
     */
    List<VehicleLocation> getVehiclesByLocation(String location);

    /**
     * <p>
     * This method is used to save the vehicle location in database.
     * </p>
     *
     * @param vehicleLocation {@link VehicleLocation}
     * @throws DatabaseException {@link  DatabaseException}
     *                           Arises when there is create anomalies in Vehicle Location database.
     */
    void saveVehicleLocation(VehicleLocation vehicleLocation);

    /**
     * <p>
     * Updates the location of a vehicle by its vehicle ID.
     * </p>
     *
     * @param location the new location of the vehicle
     * @param vehicle  the vehicle whose location needs to be updated
     * @throws DatabaseException Arises when there is update anomalies in Vehicle Location Repository.
     */
    void updateVehicleLocationByVehicleId(String location, Vehicle vehicle);

    /**
     * <p>
     * Retrieves the vehicle Location by id
     * </p>
     *
     * @param id id of the vehicle location
     * @return VehicleLocation {@link VehicleLocation}
     */
    VehicleLocation getVehicleLocationById(int id);
}
