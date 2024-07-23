package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideRequestServiceImpl implements RideRequestService{
    @Autowired
    RideRequestRepository rideRequestRepository;

    private RideRequestMapper rideRequestMapper = new RideRequestMapper();

    @Override
    public List<RideRequest> getAll() {
        return rideRequestRepository.findAll();
    }

    @Override
    public RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto) {
        return rideRequestRepository.findByCustomerNameAndRideID(selectedRideDto.getCustomerName(), selectedRideDto.getRideId());
    }

    @Override
    public RideRequest saveRideRequest(Customer customer,RideRequestDto rideRequestDto){
        RideRequest rideRequest = rideRequestMapper.requestDtoToEntity(rideRequestDto);
        rideRequest.setCustomer(customer);
        return rideRequestRepository.save(rideRequest);
    }
}
