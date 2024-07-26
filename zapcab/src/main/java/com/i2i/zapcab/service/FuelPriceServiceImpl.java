package com.i2i.zapcab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2i.zapcab.common.ZapCabConstant;
import com.i2i.zapcab.dto.FuelPriceResponse;
import com.i2i.zapcab.exception.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public FuelPriceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * <p>
     * Retrieves the current petrol price for chennai, Tamil Nadu by making a get request form the external API
     * The method sets the required header's, sends the request and parses the JSON response to extract petrol price
     * </p>
     * @return the current petrol price.
     * @throws UnexpectedException if the API request fails or the response cannot be parsed.
     */
    @Override
    public double getPetrolPrice() {
        String fullUrl = ZapCabConstant.FUEL_PRICE_API_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", ZapCabConstant.FUEL_PRICE_API_TOKEN);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.info("Sending request to fetch the petrol price from URL: {}", fullUrl);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                FuelPriceResponse fuelPriceResponse = objectMapper.readValue(response.getBody(), FuelPriceResponse.class);
                logger.info("Successfully fetched petrol price: {}", fuelPriceResponse.getPetrolPrice());
                return fuelPriceResponse.getPetrolPrice();
            } catch (Exception e) {
                logger.error("Failed to parse fuel price response.", e);
                throw new UnexpectedException("Failed to parse fuel price response");
            }
        } else {
            logger.error("Failed to fetch petrol price.");
            throw new UnexpectedException("Failed to fetch petrol price");
        }
    }
}