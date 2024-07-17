package com.i2i.zapcab.constant;

public class ZapCabConstant {
    //Regex
    public static final String STRING_REGEX = "^[A-Za-z]+$";
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.com$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
    public static final String VALID_GENDER = ("male|female|m|f|others");
    public static final String MOBILE_NUMBER_REGEX = "(0|91)?[6-9][0-9]{9}";
    public static final String LICENSE_NUMBER_REGEX = "^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9]" +
            "[0-9])[0-9]{7}$";
    public static final String LICENSE_PLATE_REGEX = ("([A-Z]{2}[ -][0-9]{1,2}[ -][A-Z]{1,2}[ -][0-9]{4})");

    //Message
    public static final String NAME_NOT_BLANK = "Name is mandatory";
    public static final String NAME_PATTERN_MESSAGE = "Name must contain only alphabetic characters";
    public static final String REGION_NOT_BLANK = "Region is mandatory";
    public static final String REGION_PATTERN_MESSAGE = "Region must contain only alphabetic characters";
    public static final String EMAIL_NOT_BLANK = "Email is mandatory";
    public static final String EMAIL_PATTERN_MESSAGE = "Email must contain .com and @";
    public static final String PASSWORD_NOT_BLANK = "Password is mandatory";
    public static final String PASSWORD_SIZE = "Password must have at-least 6 characters";
    public static final String PASSWORD_PATTERN_MESSAGE = "Password must contain at-least " +
            "one Alphabet, one number and one special character";
    public static final String LICENSE_NUMBER_NOT_BLANK = "License number is mandatory";
    public static final String RC_BOOK_NOT_BLANK = "RC Book number is mandatory";
    public static final String DOB_NOT_NULL = "Date of birth is mandatory";
    public static final String DOB_PAST_MESSAGE = "Date of birth must be a past date";
    public static final String MOBILE_NUMBER_NOT_BLANK = "Mobile Number is mandatory";
    public static final String GENDER_NOT_BLANK = "Gender is mandatory";
    public static final String GENDER_PATTERN_MESSAGE = "Gender is not valid";
    public static final String MOBILE_NUMBER_PATTERN_MESSAGE = "Mobile number must be a valid number";
    public static final String LICENSE_PLATE_NOT_BLANK = "License plate is mandatory";
}
