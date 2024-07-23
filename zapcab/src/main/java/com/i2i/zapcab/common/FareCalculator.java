package com.i2i.zapcab.common;

import com.i2i.zapcab.dto.RideRequestResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static com.i2i.zapcab.constant.ZapCabConstant.*;

public class FareCalculator {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculator.class);

    private static final Map<String, Integer> distances = new HashMap<>();

    static {
        distances.put("Guindy-Velachery", 10);
        distances.put("Guindy-Airport", 8);
        distances.put("Velachery-Airport", 12);
    }

    /**
     * Calculates the fare based on the pickup and drop points and vehicle category.
     *
     * @param pickup   The pickup location.
     * @param drop     The drop location.
     * @param category The vehicle category.
     * @return A RideRequestResponseDto containing the fare details.
     */
    public RideRequestResponseDto calculateFare(String pickup, String drop, String category) {
        try {
            logger.info("Calculating fare for route: {} to {}", pickup, drop);
            return fareByCategory(distances.getOrDefault(pickup + "-" + drop,
                    distances.getOrDefault(drop + "-" + pickup, 0)),
                    LocalTime.now().getHour(), category);
        } catch (Exception e) {
            logger.error("Error calculating fare: {}", e.getMessage());
            return null;
        }
    }

    /**
     * <p>
     * Calculates the fare and estimated drop time based on the distance, current hour,
     * and vehicle category.
     * </p>
     * @param distance    The distance between pickup and drop points.
     * @param currentHour The current hour of the day.
     * @param category    The vehicle category.
     * @return A RideRequestResponseDto containing the fare and estimated drop time.
     */
    private RideRequestResponseDto fareByCategory(
            int distance, int currentHour, String category) {
        RideRequestResponseDto rideRequestResponseDto = new RideRequestResponseDto();
        try {
            Map<String, Integer> categoryRates = Map.of(
                    XUV, XUV_RATE_PER_KM,
                    SEDAN, SEDAN_RATE_PER_KM,
                    MINI, MINI_RATE_PER_KM,
                    AUTO, AUTO_RATE_PER_KM,
                    BIKE, BIKE_RATE_PER_KM
            );
            Map<String, Integer> categorySpeeds = Map.of(
                    XUV, XUV_SPEED_PER_KM,
                    SEDAN, SEDAN_SPEED_PER_KM,
                    MINI, MINI_SPEED_PER_KM,
                    AUTO, AUTO_SPEED_PER_KM,
                    BIKE, BIKE_SPEED_PER_KM
            );
            boolean peakHour = (currentHour >= 8 && currentHour <= 11)
                    || (currentHour >= 18 && currentHour <= 20);
            int categoryRate = categoryRates.getOrDefault(category, 0);
            int speed = categorySpeeds.getOrDefault(category, 0);
            double fare = distance * (peakHour ? PEAK_RATE : NORMAL_RATE) * categoryRate;
            speed = peakHour ? speed - 30 : speed;
            double time = (double) distance / speed;
            String estimatedTime = String.format("%.2f hours", time);
            rideRequestResponseDto.setDistance(distance);
            rideRequestResponseDto.setVehicleCategory(category);
            rideRequestResponseDto.setFare(fare);
            rideRequestResponseDto.setEstimatedDropTime(estimatedTime);
            logger.info("Fare calculated: Category: {}, Fare: {}, Estimated Time: {}",
                    category, fare, estimatedTime);
            return rideRequestResponseDto;
        } catch (Exception e) {
            logger.error("Error calculating fare by category: {}", e.getMessage());
            return null;
        }
    }
}

