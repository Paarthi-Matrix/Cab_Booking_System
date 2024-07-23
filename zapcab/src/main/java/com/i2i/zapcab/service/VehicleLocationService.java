package com.i2i.zapcab.service;

import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VehicleLocationService {
    List<VehicleLocation> getVehiclesByLocation(String location);

    VehicleLocation getVehicleLocationById(int id);

    void updateVehicleLocationByVehicleId(String location, Vehicle vehicle);
}
