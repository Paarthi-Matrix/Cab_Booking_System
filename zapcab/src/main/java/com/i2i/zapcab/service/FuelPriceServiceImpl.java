package com.i2i.zapcab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2i.zapcab.common.ZapCabConstant;
import com.i2i.zapcab.dto.FuelPriceResponse;
import com.i2i.zapcab.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 *    This service uses an external API to get the current petrol price for chennai, Tamil Nadu.
 *    It uses RestTemplate to make an HTTP requests and jackson's ObjectMapper to parse JSON responses.
 * </p>
 */
@Service
public class FuelPriceServiceImpl implements FuelPriceService {
    private static final Logger logger = LoggerFactory.getLogger(FuelPriceServiceImpl.class);
    private final RestTemplate restTemplate;
    public FuelPriceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double getPetrolPrice() {
        String url = ZapCabConstant.FUEL_PRICE_API_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", ZapCabConstant.FUEL_PRICE_API_TOKEN);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.info("Sending request to fetch the petrol price from URL: {}", url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                FuelPriceResponse fuelPriceResponse = objectMapper.readValue(response.getBody(), FuelPriceResponse.class);
                logger.info("Successfully fetched petrol price: {}", fuelPriceResponse.getPetrolPrice());
                return fuelPriceResponse.getPetrolPrice();
            } catch (Exception e) {
                logger.error("Failed to parse fuel price response.", e);
                throw new DatabaseException("Failed to parse fuel price response");
            }
        } else {
            logger.error("Failed to fetch petrol price.");
            throw new DatabaseException("Failed to fetch petrol price");
        }
    }
}