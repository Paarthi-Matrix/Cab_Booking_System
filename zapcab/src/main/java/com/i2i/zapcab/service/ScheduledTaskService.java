package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Implementing classes should provide the logic for retrieving the most recent petrol price,
 * which is fetched and managed periodically by a scheduled task.
 * </p>
 */
@Component
public interface ScheduledTaskService {

    /**
     * <p>
     * Retrieves the latest petrol price.
     * Implementing classes should provide the actual logic for fetching and returning the most recent
     * petrol price as maintained by the scheduled tasks.
     * </p>
     * @return the most recently fetched petrol price
     */
    public double getLatestPetrolPrice();
}
