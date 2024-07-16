package com.i2i.zapcab.constant;

public class ZapCabConstant {
    //Regex
    public static final String STRING_REGEX = "^[A-Za-z]+$";
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.com$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

    //Message
    public static final String VALID_NAME_PATTERN_MESSAGE = "It must contain only alphabetic characters";
    public static final String VALID_EMAIL_PATTERN_MESSAGE = "Email must contain .com and @";
    public static final String PASSWORD_REQUIREMENTS_MESSAGE = "Password must contain at least one letter, one number" +
                                                                                    "and one special character";
    public static final String VALID_NUMBER_PATTERN_MESSAGE = "Contact number must contain only numbers";
    public static final String FIELD_REQUIRED_MESSAGE = "This field  is mandatory";
}
