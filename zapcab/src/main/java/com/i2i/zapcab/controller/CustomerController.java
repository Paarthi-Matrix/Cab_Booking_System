package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/vehicles")
    public ResponseEntity<?> getAvailableVehiclesWithFare(
            @RequestBody CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        try {
            List<RideRequestResponseDto> rideRequestResponseDtos = customerService.
                    getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
            if (!rideRequestResponseDtos.isEmpty()) {
                return ResponseEntity.ok("result" + rideRequestResponseDtos);
            } else {
                return new ResponseEntity<>("No data found", HttpStatus.NOT_FOUND);
            }
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rides")
    public ResponseEntity<?> saveRideRequest(@RequestAttribute("userId") int id, @RequestBody RideRequestDto rideRequestDto) {
        try {
            return new ResponseEntity<>(customerService.saveRideRequest(id,rideRequestDto),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("drivers/{id}")
    public ResponseEntity<?> updateDriverRating(
            @RequestParam int id,@RequestBody RideRatingDto ratings){
        try{
            if (customerService.updateDriverRating(id,ratings)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}