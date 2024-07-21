package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.i2i.zapcab.constant.ZapCabConstant.*;

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VehicleService vehicleService;
    private static final Map<String, Integer> distances = new HashMap<>();

    static {
        distances.put("Guindy-Velachery", 10);
        distances.put("Guindy-Airport", 8);
        distances.put("Velachery-Airport", 12);
    }

    public List<RideRequestResponseDto> getAvailableVehiclesWithFare(
            CheckVehicleAvailabilityDto checkVehicleAvailabilityDto) {
        List<RideRequestResponseDto> rideRequestResponseDtos = new ArrayList<>();
        vehicleService.getVehiclesByLocation(checkVehicleAvailabilityDto.getPickupPoint()).
                forEach((vehicles) -> {
                    RideRequestResponseDto rideRequestResponseDto = calculateFare(
                            checkVehicleAvailabilityDto.getPickupPoint(),
                            checkVehicleAvailabilityDto.getDropPoint(),
                            vehicles.getVehicle().getCategory());
                    rideRequestResponseDtos.add(rideRequestResponseDto);
                });
        return rideRequestResponseDtos;
    }

    private RideRequestResponseDto calculateFare(String pickup, String drop, String category) {
        return fareByCategory(distances.getOrDefault(pickup + "-" + drop,
                        distances.getOrDefault(drop + "-" + pickup, 0)),
                LocalTime.now().getHour(), category);
    }

    private RideRequestResponseDto fareByCategory(double distance, int currentHour, String category) {
        RideRequestResponseDto rideRequestResponseDto = new RideRequestResponseDto();
        int categoryRate = 0;
        double fare = 0;
        int speed = 0;
        boolean peakHour = ((currentHour >= 8 && currentHour <= 11)
                || (currentHour >= 18 && currentHour <= 20));
        if (category.equals(XUV)) {
            categoryRate = XUV_RATE_PER_KM;
            speed = XUV_SPEED_PER_KM;
        } else if (category.equals(SEDAN)) {
            categoryRate = SEDAN_RATE_PER_KM;
            speed = SEDAN_SPEED_PER_KM;
        } else if (category.equals(MINI)) {
            categoryRate = MINI_RATE_PER_KM;
            speed = MINI_SPEED_PER_KM;
        } else if (category.equals(AUTO)) {
            categoryRate = AUTO_RATE_PER_KM;
            speed = AUTO_SPEED_PER_KM;
        } else if (category.equals(BIKE)) {
            categoryRate = BIKE_RATE_PER_KM;
            speed = BIKE_SPEED_PER_KM;
        }
        if (peakHour) {
            fare = distance * PEAK_RATE * categoryRate;
        } else {
            fare = distance * NORMAL_RATE * categoryRate;
        }
        speed = peakHour? speed-30: speed;
        double time = distance / speed;
        String estimatedTime = time + " hours";
        rideRequestResponseDto.setVehicleCategory(category);
        rideRequestResponseDto.setFare(fare);
        rideRequestResponseDto.setEstimatedDropTime(estimatedTime);
        return rideRequestResponseDto;
    }
}
