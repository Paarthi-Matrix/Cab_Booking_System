package com.i2i.zapcab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2i.zapcab.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
