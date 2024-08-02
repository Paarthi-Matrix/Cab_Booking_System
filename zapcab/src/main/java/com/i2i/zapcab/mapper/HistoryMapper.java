package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.model.RideHistory;
import com.i2i.zapcab.model.Ride;

import java.time.LocalDate;

public class HistoryMapper {

    public RideHistoryResponseDto entityToDto(RideHistory history) {
        return RideHistoryResponseDto.builder().date(history.getDate())
                .pickup(history.getPickupPoint())
                .drop(history.getDropPoint())
                .startTime(history.getStartTime())
                .endTime(history.getEndTime())
                .distance(history.getDistance())
                .fare(history.getFare()).build();
    }

    public RideHistory rideToHistory(Ride ride) {
        return RideHistory.builder().date(LocalDate.now())
                .pickupPoint(ride.getRideRequest().getPickupPoint())
                .dropPoint(ride.getDropPoint())
                .startTime(ride.getStartTime())
                .endTime(ride.getEndTime())
                .distance(ride.getDistance())
                .user(ride.getRideRequest().getCustomer().getUser()).build();
    }
}