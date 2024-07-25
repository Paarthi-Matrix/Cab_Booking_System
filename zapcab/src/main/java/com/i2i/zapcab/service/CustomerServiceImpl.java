package com.i2i.zapcab.service;

import com.i2i.zapcab.common.FareCalculator;
import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.helper.OTPService;
import com.i2i.zapcab.mapper.RideRequestMapper;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    private final OTPService otpService = new OTPService();

    @Override
    public VehicleAvailabilityResponseDto getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = new VehicleAvailabilityResponseDto();
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        try {
            if(checkVehicleAvailabilityDto.getPickupPoint().equalsIgnoreCase(checkVehicleAvailabilityDto.getDropPoint())){
                return null;
            }
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
            vehicleAvailabilityResponseDto.setPickup(checkVehicleAvailabilityDto.getPickupPoint());
            vehicleAvailabilityResponseDto.setDrop(checkVehicleAvailabilityDto.getDropPoint());
            vehicleAvailabilityResponseDto.setRideRequestResponseDtos(rideRequestResponseDtos);
            return vehicleAvailabilityResponseDto;
        } catch (Exception e) {
            throw new UnexpectedException("Error occurred while fetching vehicles and calculating fare", e);
        }
    }

    @Override
    public boolean saveRideRequest(String id, RideRequestDto rideRequestDto) {
        try {
            return rideRequestService.saveRideRequest(customerRepository.findByUserId(id), rideRequestDto);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while saving ride request", e);
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean updateRideAndDriverRating(int id, RideRatingDto ratings) {
        try {
            return driverService.updateDriverRating(rideService.updateRideRating(id, ratings),
                    ratings.getRatings());
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while Updating Ride And Driver Ratings", e);
        }
    }

    @Override
    public AssignedDriverDto getAssignedDriverDetails(String id){
        try {
            RideRequest rideRequest = rideRequestService.checkStatusAssignedOrNot(id);
            Ride ride = rideService.getRideByRideRequest(rideRequest.getId());
            String otp = otpService.generateOTP(id);
            AssignedDriverDto assignedDriverDto = AssignedDriverDto.builder().Otp(otp).
                    name(ride.getDriver().getUser().getName()).
                    mobileNumber(ride.getDriver().getUser().getMobileNumber()).
                    ratings(ride.getDriver().getRatings()).
                    category(ride.getDriver().getVehicle().getCategory()).
                    model(ride.getDriver().getVehicle().getModel()).
                    licensePlate(ride.getDriver().getVehicle().getLicensePlate()).build();
            return !ObjectUtils.isEmpty(rideRequest) ? assignedDriverDto : null;
        }catch(Exception e){
            throw new UnexpectedException("Error Occurred while fetch assigned driver for the customer with id :"+id,e);
        }
    }
}
