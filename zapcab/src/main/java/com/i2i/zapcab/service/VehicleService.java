package com.i2i.zapcab.service;

import com.i2i.zapcab.model.VehicleLocation;

import java.util.List;

public interface VehicleService {
    List<VehicleLocation> getVehiclesByLocation(String location);
}
