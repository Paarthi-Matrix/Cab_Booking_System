package com.i2i.zapcab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i2i.zapcab.model.Ride;

public interface RideRepository extends JpaRepository<Ride, String> {
    Ride findRideByRideRequestId(String id);

    Optional<Ride> findByDriverId(String driverId);

    @Query("from Ride r where r.driver.user.id = :driverId and r.isDeleted = false")
    Optional<Ride> findByDriverIdAndIsDeleted(@Param("driverId") String driverId);
}
