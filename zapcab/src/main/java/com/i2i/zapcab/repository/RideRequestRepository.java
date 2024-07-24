package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Integer> {

    @Query("SELECT rr FROM RideRequest rr WHERE rr.customer.user.name = :customerName AND rr.id = :rideID")
    RideRequest findByCustomerNameAndRideID(@Param("customerName") String customerName, @Param("rideID") int rideID);
}
