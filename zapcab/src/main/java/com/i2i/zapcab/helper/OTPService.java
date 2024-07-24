package com.i2i.zapcab.helper;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OTPService {
        private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
        private ConcurrentMap<String, OTPDetails> otpStorage = new ConcurrentHashMap<>();
        private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        public String generateOTP(String key) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            OTPDetails otpDetails = new OTPDetails(otp, System.currentTimeMillis() + OTP_VALID_DURATION);
            otpStorage.put(key, otpDetails);

            // Schedule a task to remove the OTP after its expiry
            scheduler.schedule(() -> otpStorage.remove(key), OTP_VALID_DURATION, TimeUnit.MILLISECONDS);

            return otp;
        }

        public boolean validateOTP(String key, String otp) {
            OTPDetails otpDetails = otpStorage.get(key);
            if (otpDetails == null) {
                return false; // No OTP found for the key
            }
            if (System.currentTimeMillis() > otpDetails.getExpiryTime()) {
                otpStorage.remove(key); // Remove expired OTP
                return false; // OTP expired
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
