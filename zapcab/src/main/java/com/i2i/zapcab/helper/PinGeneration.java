package com.i2i.zapcab.helper;

import java.security.SecureRandom;

public class PinGeneration {

    public static String pinGeneration() {
        SecureRandom secureRandom = new SecureRandom();
        int pin = 10000000 + secureRandom.nextInt(90000000);
        return String.format("%8d", pin);
    }

    public static boolean pinVerification(String generatedPin, String customerPin) {
        String firstPart = generatedPin.substring(0, generatedPin.length() / 2);
        String fullCustomerPin = firstPart + customerPin;
        return generatedPin.equals(fullCustomerPin);
    }
}
