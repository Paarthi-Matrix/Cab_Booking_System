package com.i2i.zapcab.service;

import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.VehicleLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    VehicleLocationRepository vehicleLocationRepository;

    @Override
    public List<VehicleLocation> getALlVehicleLocation() {
        return vehicleLocationRepository.findAll();
    }
}
