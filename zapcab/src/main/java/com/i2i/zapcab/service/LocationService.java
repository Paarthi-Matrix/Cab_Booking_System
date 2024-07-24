package com.i2i.zapcab.service;

import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LocationService {
    public List<VehicleLocation> getALlVehicleLocation();
}
