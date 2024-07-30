package com.i2i.zapcab.helper;

import org.apache.commons.lang3.RandomStringUtils;

public class PinGeneration {
    public static String driverPasswordGeneration() {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
