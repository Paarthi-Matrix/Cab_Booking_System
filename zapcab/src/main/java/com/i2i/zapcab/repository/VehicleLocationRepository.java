package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleLocationRepository  extends JpaRepository<VehicleLocation, Integer>{
    VehicleLocation findByVehicleId(int id);
    List<VehicleLocation> findAllByLocation(String location);
}
