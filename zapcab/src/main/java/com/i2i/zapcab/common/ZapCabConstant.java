package com.i2i.zapcab.common;

/**
 * <p>
 * These are the constants that are used throughout the application.
 * </p>
 */
public class ZapCabConstant {
    //Regex
    public static final String STRING_REGEX = "^[A-Za-z\\s]+$";
    public static final String STRING_AND_NUMBER_REGEX = "^[a-zA-Z0-9_.-]*$";
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.com$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])" +
            "[A-Za-z\\d@$!%*?&]+$";
    public static final String VALID_GENDER = ("male|female|m|f|others");
    public static final String MOBILE_NUMBER_REGEX = "(0|91)?[6-9][0-9]{9}";
    public static final String LICENSE_NUMBER_REGEX = "^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))" +
            "((19|20)[0-9][0-9])[0-9]{7}$";
    public static final String LICENSE_PLATE_REGEX = ("([A-Z]{2}[ -][0-9]{1,2}[ -][A-Z]" +
            "{1,2}[ -][0-9]{4})");

    // Message
    public static final String LICENSE_NUMBER_MESSAGE = "License number must be as like DL-1420110012345 or DL14 20110012345";
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
    public static final String PICKUP_POINT_NOT_BLANK = "PickupPoint is mandatory";
    public static final String PICKUP_POINT_PATTERN_MESSAGE = "PickupPoint must contain only alphabetic characters";
    public static final String DROP_POINT_NOT_BLANK = "DropPoint is mandatory";
    public static final String DROP_POINT_PATTERN_MESSAGE = "DropPoint must contain only alphabetic characters";
    //Cab Category
    public static final String XUV = "XUV";
    public static final String SEDAN = "SEDAN";
    public static final String MINI = "MINI";
    public static final String AUTO = "AUTO";
    public static final String BIKE = "BIKE";

    //Category wise rate
    public static final int XUV_RATE_PER_KM = 15;
    public static final int SEDAN_RATE_PER_KM = 10;
    public static final int MINI_RATE_PER_KM = 8;
    public static final int AUTO_RATE_PER_KM = 5;
    public static final int BIKE_RATE_PER_KM = 3;

    //Category wise speed
    public static final int XUV_SPEED_PER_KM = 60;
    public static final int SEDAN_SPEED_PER_KM = 50;
    public static final int MINI_SPEED_PER_KM = 50;
    public static final int AUTO_SPEED_PER_KM = 50;
    public static final int BIKE_SPEED_PER_KM = 50;

    //fare
    public static final int NORMAL_RATE = 2;
    public static final int PEAK_RATE = 3;

    //Common
    public static final int AUTO_MAX_SEATS = 3;
    public static final int SEDAN_OR_XUV_MAX_SEATS = 4;
    public static final int BIKE_MAX_SEATS = 1;
    public static final String REQUEST_STATUS = "Pending";
    public static final String ASSIGNED = "Assigned";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_HEADER = "Bearer ";
    public static final String INITIAL_STATUS_OF_DRIVER = "Pending";
    public static final String INITIAL_CUSTOMER_TIRE = "Bronze";
    public static final String INITIAL_REMARKS = " ";
    public static final String VEHICLE_STATUS_UNAVAILABLE = "Unavailable";
    public static final String VEHICLE_AVAILABLE_STATUS = "Available";
    public static final String INITIAL_DRIVER_STATUS = "OFF-DUTY";
    public static final String DRIVER_STATUS = "ON-DUTY";
    public static final String RIDE_BOOKED = "Booked";
    public static final String RIDE_STARTED = "Started";
    public static final String RIDE_COMPLETED = "Completed";
    public static final String RIDE_CANCELLED = "Cancelled";
    public static final String REJECTED = "Rejected";
    public static final String TEMPORARILY_SUSPENDED = "TEMPORARILY_SUSPENDED";
    public static final double INITIAL_WALLET_AMOUNT = 200.00;
    public static final String DEFAULT_PAYMENT_MODE = "CASH";
    public static final double ZAPCAB_RIDE_COMMISSION_PERCENTAGE = 20.0/100;// 20% commission on every ride
    public static final String TEMPORARILY_UNAVAILABLE = "Temporarily Unavailable";

    //Otp duration
    public static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 30 minutes

    //Custom-Query
    public static final String FIND_DRIVER_BY_MOBILE_NUMBER_QUERY = "SELECT d FROM Driver d JOIN d.user u WHERE u.mobileNumber = :mobileNumber";
    public static final String FIND_BY_ROLE_TYPE_QUERY = "SELECT r FROM Role r WHERE r.roleName IN :roleEnum";
    public static final String FIND_BY_STATUS_QUERY = "SELECT pr from PendingRequest pr WHERE pr.status =:status";
    public static final String FIND_BY_CUSTOMER_NAME_AND_RIDE_ID = "SELECT rr FROM RideRequest rr WHERE rr.customer.user.name = :customerName AND rr.id = :rideID";
    public static final String FIND_BY_CUSTOMER_ID = "from RideRequest r JOIN r.customer c where c.id = :customerId and r.isDeleted = false";
    public static final String COUNT_BY_USER_ID = "SELECT COUNT(h) FROM RideHistory h WHERE h.user.id = :userId";

    //EMAIL
    public static final String EMAIL_SUBJECT_FOR_DRIVE_ON_SUCCESSFUL_REGISTRATION = "Welcome to ZapCab: Your Driver Account Details";
    public static final String EMAIL_SUBJECT_FOR_DRIVE_ON_REJECTION = "ZapCab: Rejection of your request";
    public static final String EMAIL_SUBJECT_FOR_CUSTOMER = "Welcome to ZapCab: Let your Journey begins here with us!";
    public static final String EMAIL_SUBJECT_FOR_CUSTOMER_INVOICE = "ZapCab : Invoice details. Happy Journey ";
    public static final String APP_DOMAIN_NAME = "www.zapcab.com";

    //Petrol-Price
    public static final int ASSUMED_PETROL_PRICE = 100;
    public static final int PETROL_CHARGES = 1;

    //Payment
    public static final String PAYMENT_CASH = "cash";
}
