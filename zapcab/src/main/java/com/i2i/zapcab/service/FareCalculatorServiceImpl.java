package com.i2i.zapcab.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.exception.DatabaseException;

import static com.i2i.zapcab.common.ZapCabConstant.ASSUMED_PETROL_PRICE;
import static com.i2i.zapcab.common.ZapCabConstant.AUTO;
import static com.i2i.zapcab.common.ZapCabConstant.AUTO_RATE_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.AUTO_SPEED_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.BIKE;
import static com.i2i.zapcab.common.ZapCabConstant.BIKE_RATE_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.BIKE_SPEED_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.MINI;
import static com.i2i.zapcab.common.ZapCabConstant.MINI_RATE_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.MINI_SPEED_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.NORMAL_RATE;
import static com.i2i.zapcab.common.ZapCabConstant.PEAK_RATE;
import static com.i2i.zapcab.common.ZapCabConstant.PETROL_CHARGES;
import static com.i2i.zapcab.common.ZapCabConstant.SEDAN;
import static com.i2i.zapcab.common.ZapCabConstant.SEDAN_RATE_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.SEDAN_SPEED_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.XUV;
import static com.i2i.zapcab.common.ZapCabConstant.XUV_RATE_PER_KM;
import static com.i2i.zapcab.common.ZapCabConstant.XUV_SPEED_PER_KM;

public class FareCalculatorServiceImpl implements FareCalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorServiceImpl.class);
    private static final Map<String, Integer> distances = new HashMap<>();

    static {
        distances.put("Guindy-Velachery", 10);
        distances.put("Guindy-Airport", 8);
        distances.put("Velachery-Airport", 12);
    }

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    public RideRequestResponseDto calculateFare(String pickup, String drop, String category) {
        logger.info("Calculating fare for route: {} to {}", pickup, drop);
        int airportCharge = 0;
        airportCharge = (pickup.equalsIgnoreCase("Airport")
                || drop.equalsIgnoreCase("Airport")) ? 100 : 0;
        return fareByCategory(distances.getOrDefault(pickup + "-" + drop,
                        distances.getOrDefault(drop + "-" + pickup, 0)),
                LocalTime.now().getHour(), category.toUpperCase(), airportCharge);
    }

    /**
     * <p>
     * Calculates the fare and estimated drop time based on the distance, current hour,
     * and vehicle category.
     * </p>
     *
     * @param distance    The distance between pickup and drop points.
     * @param currentHour The current hour of the day.
     * @param category    The vehicle category.
     * @return A RideRequestResponseDto containing the fare and estimated drop time.
     * @throws DatabaseException if any error occurs while calculating fare by category.
     */
    private RideRequestResponseDto fareByCategory(
            int distance, int currentHour, String category, int airportCharge) {
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
            double petrolPrice = scheduledTaskService.getLatestPetrolPrice();
            categoryRate = (petrolPrice > ASSUMED_PETROL_PRICE) ? categoryRate + PETROL_CHARGES : categoryRate;
            int speed = categorySpeeds.getOrDefault(category, 0);
            double fare = distance * (peakHour ? PEAK_RATE : NORMAL_RATE) * categoryRate + airportCharge;
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
            throw new DatabaseException("Error Occurred while Calculating fare by vehicle category", e);
        }
    }
}