package com.i2i.zapcab.service;

import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *     An interface that performs operations related to the location of the vehicle.
 * </p>
 */
@Component
public interface VehicleLocationService {
    List<VehicleLocation> getVehiclesByLocation(String location);
    void saveVehicleLocation(VehicleLocation vehicleLocation);
    VehicleLocation getVehicleLocationById(int id);
    void updateVehicleLocationByVehicleId(String location, Vehicle vehicle);
}
