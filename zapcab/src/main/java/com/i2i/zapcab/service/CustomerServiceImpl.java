package com.i2i.zapcab.service;

import com.i2i.zapcab.common.FareCalculator;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VehicleLocationService vehicleLocationService;
    @Autowired
    private RideRequestService rideRequestService;
    @Autowired
    private RideService rideService;
    @Autowired
    private DriverService driverService;

    private final FareCalculator fareCalculator = new FareCalculator();

    @Override
    public List<RideRequestResponseDto> getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        try {
            logger.info("Fetching vehicles for pickup point: {}",
                    checkVehicleAvailabilityDto.getPickupPoint());
            vehicleLocationService.getVehiclesByLocation(
                            checkVehicleAvailabilityDto.getPickupPoint())
                    .forEach(vehicle -> {
                        RideRequestResponseDto rideRequestResponseDto = fareCalculator.calculateFare(
                                checkVehicleAvailabilityDto.getPickupPoint(),
                                checkVehicleAvailabilityDto.getDropPoint(),
                                vehicle.getVehicle().getCategory());
                        rideRequestResponseDtos.add(rideRequestResponseDto);
                    });
            return rideRequestResponseDtos;
        } catch (Exception e) {
            logger.error("Error fetching available vehicles: {}", e.getMessage());
            throw new AuthenticationException("Error occurred while fetching vehicles and calculating fare",e);
        }
    }

    public RideRequest saveRideRequest(int id ,RideRequestDto rideRequestDto){
        try{
            return rideRequestService.saveRideRequest(customerRepository.findById(id).get(),rideRequestDto);
        }catch (Exception e){
            logger.error("Error Occurred while adding ride request {}" , e.getMessage());
            return null;
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean updateDriverRating(int id, RideRatingDto ratings){
        return driverService.updateDriverRating(rideService.updateRideRating(id,ratings),
                ratings.getRatings());
    }
}
