package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, String> {
    Ride findRideByRideRequestId(String id);
    Optional<Ride> findByDriverId(String driverId);
}
