package com.i2i.zapcab.helper;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.i2i.zapcab.common.ZapCabConstant.OTP_VALID_DURATION;

/**
 * <p>
 * Service class for generating and validating One-Time Passwords (OTPs).
 * </p>
 */
public class OTPService {
    private ConcurrentMap<String, OTPDetails> otpStorage = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * <p>
     * Generates a new OTP and stores it with an expiry time.
     * </p>
     *
     * @param key The unique key for storing the OTP.
     * @return The generated OTP as a string.
     */
    public String generateOTP(String key) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        OTPDetails otpDetails = new OTPDetails(otp, System.currentTimeMillis() +
                OTP_VALID_DURATION);
        otpStorage.put(key, otpDetails);
        scheduler.schedule(() -> otpStorage.remove(key), OTP_VALID_DURATION, TimeUnit.MILLISECONDS);
        return otp;
    }

    /**
     * <p>
     * Validates an OTP for a given key.
     * </p>
     *
     * @param key The unique key associated with the OTP.
     * @param otp The OTP to be validated.
     * @return true if the OTP is valid and has not expired, otherwise false.
     */
    public boolean validateOTP(String key, String otp) {
        OTPDetails otpDetails = otpStorage.get(key);
        if (otpDetails == null) {
            return false;
        }
        if (System.currentTimeMillis() > otpDetails.getExpiryTime()) {
            otpStorage.remove(key);
            return false;
        }
        return otp.equals(otpDetails.getOtp());
    }

    private static class OTPDetails {
        private String otp;
        private long expiryTime;

        public OTPDetails(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}
