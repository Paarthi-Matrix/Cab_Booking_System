package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserId(String id);

    // @Query("SELECT d FROM Driver d JOIN d.vehicle v WHERE d.vehicle.category = :category AND v.id = :vehicleId")
    // public List<Driver> findByCategory(@Param("category") String category);

    @Query("SELECT d FROM Driver d JOIN d.user u WHERE u.mobileNumber = :mobileNumber")
    Driver findDriverByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
