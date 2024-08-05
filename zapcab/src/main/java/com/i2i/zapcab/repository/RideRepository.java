package com.i2i.zapcab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i2i.zapcab.model.Ride;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, String> {
    Ride findRideByRideRequestId(String id);

    @Query("from Ride r where r.driver.id = :driverId and r.isDeleted = false")
    Optional<Ride> findByDriverId(@Param("driverId") String driverId);

    @Query("from Ride r where r.driver.user.id = :userId and r.isDeleted = false")
    Optional<Ride> findByDriverIdAndIsDeleted(@Param("userId") String userId);

    String findStatusById(String driverId);
}
