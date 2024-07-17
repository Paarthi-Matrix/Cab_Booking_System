package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CustomerServiceImpl {

    @Autowired
    private CustomerRepository customerRepository;

    public RideResponseDto getCustomerBookingResult(String id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (!customerOptional.isPresent()) {
            return null;
        }

        Customer customer = customerOptional.get();

        return null;
    }

}
