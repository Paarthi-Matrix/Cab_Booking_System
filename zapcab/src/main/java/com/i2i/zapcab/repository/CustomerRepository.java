package com.i2i.zapcab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.i2i.zapcab.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUserId(String id);
}
