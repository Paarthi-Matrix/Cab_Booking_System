package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.mapper.RideRequestMapper;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.i2i.zapcab.common.ZapCabConstant.REQUEST_STATUS;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.ASSIGNED;
import static com.i2i.zapcab.common.ZapCabConstant.REQUEST_STATUS;

@Service
public class RideRequestServiceImpl implements RideRequestService {
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
    public void updateRequest(RideRequest rideRequest) {
        rideRequestRepository.save(rideRequest);
    }

    @Override
    public boolean saveRideRequests(Customer customer, RideRequestDto rideRequestDto) {
        try {
            RideRequest rideRequest = rideRequestMapper.requestDtoToEntity(rideRequestDto);
            rideRequest.setStatus(REQUEST_STATUS);
            rideRequest.setRideTime(LocalTime.now().getHour());
            rideRequest.setCustomer(customer);
            return !ObjectUtils.isEmpty(rideRequestRepository.save(rideRequest));
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while saving ride request", e);
        }
    }

    @Override
    public void updateRideRequestStatus(int id) {
        try {
            RideRequest rideRequest = rideRequestRepository.findById(id).get();
            rideRequest.setStatus(ASSIGNED);
            rideRequestRepository.save(rideRequest);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while updating ride request status", e);
        }
    }

    @Override
    public RideRequest checkStatusAssignedOrNot(String id) {
        try {
            Optional<RideRequest> rideRequest = rideRequestRepository.findByCustomerId(id);
            return rideRequest.orElse(null);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while checking ride request status is assigned or not", e);
        }
    }
    public RideRequest saveRideRequest(Customer customer,RideRequestDto rideRequestDto){
        RideRequest rideRequest = rideRequestMapper.requestDtoToEntity(rideRequestDto);
        rideRequest.setStatus(REQUEST_STATUS);
        rideRequest.setCustomer(customer);
        return rideRequestRepository.save(rideRequest);
    }
}
