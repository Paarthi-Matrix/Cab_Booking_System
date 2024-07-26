package com.i2i.zapcab.service;

import com.i2i.zapcab.model.VehicleLocation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *    An interface that maintains the location related operation
 * </p>
 */
@Component
public interface LocationService {
    public List<VehicleLocation> getALlVehicleLocation();
}
