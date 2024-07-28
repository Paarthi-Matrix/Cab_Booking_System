package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.model.History;

public class HistoryMapper {

    public RideHistoryResponseDto entityToDto(History history){
        return RideHistoryResponseDto.builder().date(history.getDate())
                .pickup(history.getPickupPoint())
                .drop(history.getDropPoint())
                .startTime(history.getStartTime())
                .endTime(history.getEndTime())
                .distance(history.getDistance())
                .fare(history.getFare()).build();
    }
}