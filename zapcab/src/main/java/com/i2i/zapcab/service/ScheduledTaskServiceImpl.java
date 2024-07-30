package com.i2i.zapcab.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.exception.DatabaseException;

/**
 * <p>
 * This service is responsible for periodically retrieving the latest petrol price using the
 * {@link FuelPriceService} and storing it for later use. It uses Spring's scheduling capabilities
 * to perform the task at a defined interval.
 * </p>
 */
@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskServiceImpl.class);
    private final FuelPriceService fuelPriceService;
    private double latestPetrolPrice;

    public ScheduledTaskServiceImpl(FuelPriceService fuelPriceService) {
        this.fuelPriceService = fuelPriceService;
    }

    /**
     * <p>
     * Initializes the service by fetching the current petrol price.
     * This method is called once when the bean is fully initialized.
     * </p>
     */
    @PostConstruct
    public void initializePetrolPrice() {
        logger.info("Initializing petrol price.");
        fetchPetrolPrice();
    }

    /**
     * <p>
     * Scheduled method to fetch the petrol price at midnight every day.
     * This method uses a cron expression to define the schedule.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchPetrolPriceAtMidnight() {
        logger.info("Fetching petrol price at midnight.");
        fetchPetrolPrice();
    }

    /**
     * <p>
     * Fetches the latest petrol price and updates the {@code latestPetrolPrice} field.
     * </p>
     *
     * @throws DatabaseException if there is an error fetching the petrol price.
     */
    private void fetchPetrolPrice() {
        try {
            latestPetrolPrice = fuelPriceService.getPetrolPrice();
            logger.info("Successfully fetched latest petrol price.");
        } catch (Exception e) {
            logger.error("Failed to fetch petrol price", e);
            throw new DatabaseException("Failed to fetch petrol price");
        }
    }

    @Override
    public double getLatestPetrolPrice() {
        logger.info("Retrieving the latest petrol price: {}", latestPetrolPrice);
        return latestPetrolPrice;
    }
}
