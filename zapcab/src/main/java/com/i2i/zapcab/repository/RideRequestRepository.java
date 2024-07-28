package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.FIND_BY_CUSTOMER_ID;
import static com.i2i.zapcab.common.ZapCabConstant.FIND_BY_CUSTOMER_NAME_AND_RIDE_ID;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, String> {

    @Query(FIND_BY_CUSTOMER_NAME_AND_RIDE_ID)
    RideRequest findByCustomerNameAndRideID(@Param("customerName") String customerName, @Param("rideID") int rideID);

    @Query(FIND_BY_CUSTOMER_ID)
    Optional<RideRequest> findByCustomerId(@Param("customerId") String id);
}
