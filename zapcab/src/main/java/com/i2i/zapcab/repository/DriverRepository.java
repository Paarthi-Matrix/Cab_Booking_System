package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserId(String id);
}
