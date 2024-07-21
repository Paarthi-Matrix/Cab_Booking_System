package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAvailableVehiclesWithFare(
            @RequestBody CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        return ResponseEntity.ok(customerService.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto));
    }
}