package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;

import java.util.List;

public interface CustomerService {


    List<RideRequestResponseDto> getAvailableVehiclesWithFare(CheckVehicleAvailabilityDto
                                                              checkVehicleAvailabilityDto);
}