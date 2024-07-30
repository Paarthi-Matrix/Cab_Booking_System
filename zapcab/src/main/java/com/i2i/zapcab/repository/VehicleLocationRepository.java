package com.i2i.zapcab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2i.zapcab.model.VehicleLocation;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, String> {
    VehicleLocation findByVehicleId(String id);

    List<VehicleLocation> findAllByLocation(String location);
}
