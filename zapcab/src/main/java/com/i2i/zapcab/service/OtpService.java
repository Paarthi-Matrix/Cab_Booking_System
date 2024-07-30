package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Service class for generating and validating One-Time Passwords (OTPs).
 * </p>
 */
@Component
public interface OtpService {

    /**
     * <p>
     * Generates a new OTP and stores it with an expiry time.
     * </p>
     *
     * @param key The unique key for storing the OTP.
     * @return The generated OTP as a string.
     */
    String generateOTP(String key);

    /**
     * <p>
     * Validates an OTP for a given key.
     * </p>
     *
     * @param key The unique key associated with the OTP.
     * @param otp The OTP to be validated.
     * @return true if the OTP is valid and has not expired, otherwise false.
     */
    boolean validateOTP(String key, String otp);
}