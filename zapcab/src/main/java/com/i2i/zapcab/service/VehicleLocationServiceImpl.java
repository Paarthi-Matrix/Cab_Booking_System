package com.i2i.zapcab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.VehicleLocationRepository;

import java.util.List;

@Service
public class VehicleLocationServiceImpl implements VehicleLocationService {

    @Autowired
    VehicleLocationRepository vehicleLocationRepository;
    @Override
    public VehicleLocation getVehicleLocationById(int id) {
        return null;
    }

    @Override
    public void updateVehicleLocationByVehicleId(String location, Vehicle vehicle) {
        VehicleLocation vehicleLocation = vehicleLocationRepository.findByVehicleId(vehicle.getId());
        vehicleLocation.setLocation(location);
        vehicleLocationRepository.save(vehicleLocation);
    }

    public List<VehicleLocation> getVehiclesByLocation(String location){
        return vehicleLocationRepository.findAllByLocation(location);
    }
}
