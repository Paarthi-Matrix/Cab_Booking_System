package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.model.RideRequest;

public class RideRequestMapper {
    public RideRequest requestDtoToEntity(RideRequestDto rideRequestDto) {
        return RideRequest.builder().
                pickupPoint(rideRequestDto.getPickupPoint()).
                dropPoint(rideRequestDto.getDropPoint()).
                distance(rideRequestDto.getDistance()).
                vehicleCategory(rideRequestDto.getVehicleCategory()).
                fare(rideRequestDto.getFare()).
                dropTime(rideRequestDto.getDropTime()).build();
    }

    public RequestedRideDto entityToDto(RideRequest rideRequest) {
        return RequestedRideDto.builder().
                rideId(rideRequest.getId()).
                pickUpPoint(rideRequest.getPickupPoint()).
                dropPoint(rideRequest.getDropPoint()).
                distance(rideRequest.getDistance()).
                customerName(rideRequest.getCustomer().getUser().getName()).
                mobileNumber(rideRequest.getCustomer().getUser().getMobileNumber()).build();
    }

    public RideDetailsDto entityToResponseDto(RideRequest rideRequest) {
        return RideDetailsDto.builder().
                pickupPoint(rideRequest.getPickupPoint()).
                distance(rideRequest.getDistance()).
                customerName(rideRequest.getCustomer().getUser().getName()).
                mobileNumber(rideRequest.getCustomer().getUser().getMobileNumber()).build();
    }
}
