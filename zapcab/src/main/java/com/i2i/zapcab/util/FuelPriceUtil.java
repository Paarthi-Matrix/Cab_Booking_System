package com.i2i.zapcab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2i.zapcab.dto.FuelPriceResponseDto;
import com.i2i.zapcab.exception.ApiException;

/**
 * <p>
 * This class provides methods to make HTTP requests to a fuel price API and parse
 * the response into a {@link FuelPriceResponseDto}. It utilizes {@link RestTemplate}
 * for sending HTTP requests and {@link ObjectMapper} for JSON deserialization.
 * </p>
 */
@Component
public class FuelPriceUtil {
    private static final Logger logger = LoggerFactory.getLogger(FuelPriceUtil.class);
    private static RestTemplate restTemplate;

    public FuelPriceUtil(RestTemplate restTemplate) {
        FuelPriceUtil.restTemplate = restTemplate;
    }

    /**
     * <p>
     * Sends an HTTP GET request to the specified URL with the given headers and parses
     * the response into a {@link FuelPriceResponseDto}.
     * </p>
     *
     * @param url     the URL to fetch the fuel price.
     * @param headers the HTTP headers to include in the request.
     * @return FuelPriceResponseDto {@link FuelPriceResponseDto}
     * @throws ApiException if the response cannot be fetched or parsed
     */
    public static FuelPriceResponseDto fetchFuelPrice(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.info("Sending request to fetch the fuel price from URL: {}", url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.getBody(), FuelPriceResponseDto.class);
            } catch (Exception e) {
                logger.error("Failed to parse fuel price response.", e);
                throw new ApiException("Failed to parse fuel price response");
            }
        } else {
            logger.error("Failed to fetch fuel price.");
            throw new ApiException("Failed to fetch fuel price");
        }
    }
}
