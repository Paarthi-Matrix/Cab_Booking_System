package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CustomerProfileDto;
import com.i2i.zapcab.dto.RideResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface CustomerService {
    public RideResponseDto getCustomerBookingResult(int id);

    public CustomerProfileDto getCustomerProfile(int customerId);
}
