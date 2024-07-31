package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRequestResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface FareCalculatorService {
    /**
     * <p>
     * Calculates the fare based on the pickup and drop points and vehicle category.
     * </p>
     *
     * @param pickup   The pickup location.
     * @param drop     The drop location.
     * @param category The vehicle category.
     * @return A RideRequestResponseDto containing the fare details.
     */
    RideRequestResponseDto calculateFare(String pickup, String drop, String category);
}
