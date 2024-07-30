package com.i2i.zapcab.service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String OTP_PREFIX = "otp:";
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int RATE_LIMIT = 20;
    private static final long RATE_LIMIT_PERIOD_SECONDS = 1800;

    public String generateOTP(String userId) {
        String rateLimitKey = RATE_LIMIT_PREFIX + userId;
        if (!isRateLimited(rateLimitKey)) {
            return "Rate limit exceeded. Please try again later.";
        }
        String otpKey = OTP_PREFIX + userId;
        String existingOTP = redisTemplate.opsForValue().get(otpKey);
        if (existingOTP != null) {
            return "An OTP has already been generated for this number. Please check your SMS.";
        }
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(5));
        return otpCode;
    }

    public boolean validateOTP(String userId, String otpCode) {
        String otpKey = OTP_PREFIX + userId;
        String storedOTP = redisTemplate.opsForValue().get(otpKey);
        if (storedOTP != null && storedOTP.equals(otpCode)) {
            redisTemplate.delete(otpKey);
            return true;
        }
        return false;
    }

    private boolean isRateLimited(String rateLimitKey) {
        Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey, 1);
        if (currentCount == 1) {
            redisTemplate.expire(rateLimitKey, RATE_LIMIT_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
        return currentCount <= RATE_LIMIT;
    }
}

