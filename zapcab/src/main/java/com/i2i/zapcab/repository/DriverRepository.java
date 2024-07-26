package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Driver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

    @Query("SELECT d FROM Driver d JOIN d.user u WHERE u.mobileNumber = :mobileNumber")
    Driver findDriverByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
