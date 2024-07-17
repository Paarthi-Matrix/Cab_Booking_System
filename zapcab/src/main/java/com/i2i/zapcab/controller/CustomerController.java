package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("/ride/{id}")
    public ResponseEntity<?> getRideBookingResult(@PathVariable String id) {
        RideResponseDto rideResponseDto = customerService.getCustomerBookingResult(id);

        if (null == rideResponseDto) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rideResponseDto);
    }
}
