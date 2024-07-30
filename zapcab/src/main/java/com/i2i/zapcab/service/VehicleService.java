package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.model.Vehicle;

/**
 * <p>
 * An interface provides services to the vehicle
 * </p>
 */
@Component
public interface VehicleService {
    /**
     * <p>
     * Save the vehicle to the data base
     * </p>
     *
     * @param vehicle {@link Vehicle}
     */
    void saveVehicle(Vehicle vehicle);

    /**
     * <p>
     * This method is used to updated the vehicle status
     * </p>
     *
     * @param status
     * @param vehicle
     */
    void updateVehicleStatus(String status, Vehicle vehicle);

    /**
     * <p>
     * This method is used to updated the location of the vehicle.e
     * </p>
     *
     * @param Location
     * @param vehicle
     */
    void updateVehicleLocation(String Location, Vehicle vehicle);
}