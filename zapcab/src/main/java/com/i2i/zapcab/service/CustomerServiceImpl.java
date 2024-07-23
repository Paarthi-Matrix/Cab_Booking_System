package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.exception.ZapCabException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public RideResponseDto getCustomerBookingResult(int id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return null;
        }
        Customer customer = customerOptional.get();
        return null;
    }

    public CustomerProfileDto getCustomerProfile(int customerId) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                return CustomerProfileDto.builder()
                        .name(customer.getUser().getName())
                        .email(customer.getUser().getEmail())
                        .mobileNumber(customer.getUser().getMobileNumber())
                        .gender(customer.getUser().getGender())
                        .tier(customer.getTier())
                        .build();
            } return null;
        } catch (Exception e) {
            throw new ZapCabException("Failed to fetch customer profile for ID: " + customerId, e);
        }
    }

}
