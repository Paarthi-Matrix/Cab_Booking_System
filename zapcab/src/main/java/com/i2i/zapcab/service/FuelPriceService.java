package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.exception.DatabaseException;

/**
 * <p>
 * This interface defines a method for fetching the current petrol price.
 * Implementing classes should provide the logic for interacting with external APIs or data sources to get
 * the fuel price.
 * </p>
 */
@Component
public interface FuelPriceService {

    /**
     * <p>
     * Retrieves the current petrol price.
     * Implementing classes should provide the details of how the petrol price is fetched from an external source.
     * </p>
     *
     * @return the current petrol price as a double.
     * @throws DatabaseException Arises when there is error while fetching the fuel price from third party API.
     */
    double getPetrolPrice();
}
