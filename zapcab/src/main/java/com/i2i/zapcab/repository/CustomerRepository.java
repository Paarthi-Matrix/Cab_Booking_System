package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
