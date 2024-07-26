package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, String> {

    @Query("SELECT rr FROM RideRequest rr WHERE rr.customer.user.name = :customerName AND rr.id = :rideID")
    RideRequest findByCustomerNameAndRideID(@Param("customerName") String customerName, @Param("rideID") int rideID);

    @Query("from RideRequest r JOIN r.customer c where c.user.id = :customerId and r.isDeleted = false")
    Optional<RideRequest> findByCustomerId(@Param("customerId") String id);
}
