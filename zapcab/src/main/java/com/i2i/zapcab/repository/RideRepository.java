package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Integer> {
Ride findRideByRideRequestId(int id);
}
