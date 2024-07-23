package com.i2i.zapcab.service;

import com.i2i.zapcab.model.Vehicle;
import org.springframework.stereotype.Component;
import com.i2i.zapcab.model.VehicleLocation;

import java.util.List;

@Component
public interface VehicleService {
    void saveVehicle(Vehicle vehicle);
    void updateVehicleStatus(String status, Vehicle vehicle);
    void updateVehicleLocation(String Location, Vehicle vehicle);
}
