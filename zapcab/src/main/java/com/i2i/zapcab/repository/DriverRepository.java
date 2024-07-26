package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Driver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static com.i2i.zapcab.common.ZapCabConstant.FIND_DRIVER_BY_MOBILE_NUMBER_QUERY;

import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

    @Query(FIND_DRIVER_BY_MOBILE_NUMBER_QUERY)
    Driver findDriverByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
