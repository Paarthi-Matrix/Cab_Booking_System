package com.i2i.zapcab.service;

import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VehicleServiceImpl implements VehicleService{
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleLocation> getVehiclesByLocation(String location){
        return vehicleRepository.findAllByLocation(location);
    }
}
