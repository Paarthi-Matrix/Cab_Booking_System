package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.*;

import java.util.List;
import java.util.Optional;

import com.i2i.zapcab.exception.DatabaseException;

import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;

import static com.i2i.zapcab.common.ZapCabConstant.DEFAULT_PAYMENT_MODE;

/**
 * <p>
 *     The DriverController class handles HTTP requests related to driver operations.
 *     This includes updating the driver's status and location, and changing the driver's password.
 * </p>
 * <p>
 *     The methods in this controller return ApiResponseDto objects, which encapsulate the response
 *     data and status code for the API responses.
 * </p>
 */
@RestController
@RequestMapping("/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private DriverService driverService;
    @Autowired
    private RideService rideService;
    @Autowired
    private UserService userService;
    @Autowired
    private VehicleLocationService vehicleLocationService;
    /**
     * <p>
     * This function is used to update the status and location of the driver.
     * <p>
     * NOTE : The status of the driver can be as follows,
     * </p>
     *     <ol>
     *         <li>ONDUTY</li>
     *         <li>OFFDUTY</li>
     *         <li>SUSPENDED</li>
     *     </ol>
     * </p>
     *
     * @param updateDriverStatusDto {@link UpdateDriverStatusDto}
     *                              This must contain all the values of the `UpdateDriverStatusDto`.
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me")
    public ApiResponseDto<String> updateDriverStatusAndLocation(@RequestBody UpdateDriverStatusDto updateDriverStatusDto) {
        logger.debug("Entering into the method to update the driver status and location...");
        String Userid = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Updating the status and location for the driver {} ", Userid);
            driverService.updateDriverStatusAndLocation(Userid, updateDriverStatusDto);
        } catch (NotFoundException e) {
            logger.error("Unable to update the details for the driver :{} ", Userid);
            return ApiResponseDto.statusNoContent("No such driver available");
        }
        logger.debug("Exiting the method ");
        return ApiResponseDto.statusOk("Driver location and status updated successfully!");
    }

    /**
     * <p>
     * This method is used to display the requested ride to the driver. The filtration is done based on
     * location and the vehicle category.
     * </p>
     *
     * @param getRideRequestListsDto {@link GetRideRequestListsDto}
     * @return ApiResponseDto<List < RequestedRideDto> {@link RequestedRideDto}
     */
    @GetMapping("/me/requests")
    public ApiResponseDto<List<RequestedRideDto>> getAvailableRideRequest(@RequestBody GetRideRequestListsDto getRideRequestListsDto) {
        List<RequestedRideDto> requestedRideDtos = null;
        try {
            logger.info("Fetching available ride requests with filter: {}", getRideRequestListsDto);
            requestedRideDtos = driverService.getRideRequests(getRideRequestListsDto);
            if (requestedRideDtos != null && !requestedRideDtos.isEmpty()) {
                logger.info("Found {} ride requests.", requestedRideDtos.size());
                return ApiResponseDto.statusOk(requestedRideDtos);
            } else {
                logger.info("No ride requests found.");
                return ApiResponseDto.statusNotFound(requestedRideDtos);
            }
        } catch (NotFoundException e) {
            logger.error("Ride requests not found. Error: {}", e.getMessage(), e);
            return ApiResponseDto.statusNotFound(null, e);
        } catch (DatabaseException e) {
            logger.error("Error occurred while fetching ride requests. Error: {}", e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }

    /**
     * <p>
     * This method is responsible for changing the password of the driver.
     * </p>
     *
     * @param changePasswordRequestDto {@link ChangePasswordRequestDto}
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me/password")
    public ApiResponseDto<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Attempting to change password for driver ID: {}", id);
            driverService.changePassword(id, changePasswordRequestDto.getNewPassword());
            logger.info("Password changed successfully for driver ID: {}", id);
            return ApiResponseDto.statusOk("Driver password changed successfully!");
        } catch (DatabaseException e) {
            logger.error("Error occurred while changing password for driver ID: {}. Error: {}",
                    id, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError("Unexpected error occurred while changing password", e);
        }
    }

    /**
     * <p>
     * This method is used to fetch the ride details mapped with the customer.
     * </p>
     *
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto
     * Holds the customer requested ride data {@link RideDetailsDto}
     */
    @PostMapping("/me")
    public ApiResponseDto<RideDetailsDto> acceptRide(@RequestBody DriverSelectedRideDto selectedRideDto) {
        RideDetailsDto rideDetailsDto = null;
        try {
            String userId = JwtDecoder.extractUserIdFromToken();
            logger.info("Received request to get ride details for: {}", selectedRideDto);
            rideDetailsDto = driverService.acceptRide(selectedRideDto, userId);
            logger.info("Successfully retrieved ride details: {}", rideDetailsDto);
            return ApiResponseDto.statusOk(rideDetailsDto);
        } catch (DatabaseException e) {
            logger.error("Error occurred while retrieving ride details for: {}", selectedRideDto, e);
            return ApiResponseDto.statusInternalServerError(rideDetailsDto, e);
        }
    }

    /**
     * This method is used to masks the mobile number according to the user's wish
     * If they wish not to expose the number, they can set the option to true.
     *
     * @param maskMobileNumberRequestDto {@link MaskMobileNumberRequestDto}
     * @return ApiResponseDto<MaskMobileNumberResponseDto>
     * Return a message according to the action taken
     */
    @PatchMapping("/me/mask")
    public ApiResponseDto<MaskMobileNumberResponseDto> maskMobileNumber(@RequestBody MaskMobileNumberRequestDto maskMobileNumberRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        MaskMobileNumberResponseDto maskMobileNumberResponseDto = null;
        try {
            logger.info("Attempting to mask mobile number for driver ID: {}", id);
            maskMobileNumberResponseDto = driverService.updateMaskMobileNumber(id, maskMobileNumberRequestDto);
            logger.info("Mobile number masked successfully for driver ID: {}", id);
            return ApiResponseDto.statusOk(maskMobileNumberResponseDto);
        } catch (DatabaseException e) {
            logger.error("Error occurred while masking mobile number for driver ID: {}, id",
                    e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(maskMobileNumberResponseDto, e);
        }
    }

    /**
     * <p>
     * Validates the OTP provided by the customer to start the ride.
     * </p>
     *
     * @param otpRequestDto {@link OtpRequestDto}
     * @return ApiResponseDto<OTPResponseDto>
     * provides the successful message if the OTP is correct otherwise it returns an invalid message
     */
    @PostMapping("/me/otp")
    public ApiResponseDto<OTPResponseDto> otpValidation(@RequestBody OtpRequestDto otpRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        OTPResponseDto otpResponseDto = null;
        try {
            logger.info("Received request to validate OTP for user ID: {}", id);
            if (driverService.otpValidation(otpRequestDto, id)) {
                otpResponseDto = OTPResponseDto.builder().msg("Otp validated successfully! Your ride has been started").build();
                logger.info("Successfully validated OTP for user ID: {}", id);
                return ApiResponseDto.statusOk(otpResponseDto);
            } else {
                otpResponseDto = OTPResponseDto.builder().msg("Invalid otp!!!").build();
                logger.warn("Invalid OTP provided for user ID: {}", id);
                return ApiResponseDto.statusOk(otpResponseDto);
            }
        } catch (DatabaseException e) {
            logger.error("Error occurred while validating OTP for user ID: {}", id, e);
            return ApiResponseDto.statusInternalServerError(otpResponseDto, e);
        }
    }

    /**
     * <p>
     * This method handles updating the payment mode for a driver and subsequently updating the ride status.
     * </p>
     *
     * @param paymentModeDto {@link PaymentModeDto}
     * @return ApiResponseDto<paymentMode>
     * The response entity containing the updated payment mode, or an error message.
     */
    @PatchMapping("/me/payments")
    public ApiResponseDto<?> updatePaymentMode(@RequestBody PaymentModeDto paymentModeDto) {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            String driverId = driverService.retrieveDriverIdByUserId(userId);
            RideResponseDto rideResponseDto = rideService.setPaymentMode(driverId, paymentModeDto);
            logger.info("Updated payment mode for user with ID {}", userId);
            vehicleLocationService.updateVehicleLocationByVehicleId(rideService.updateRideStatus(driverId),
                    driverService.getVehicleIdByDriverId(driverId));
            if (paymentModeDto.getPaymentMode().equalsIgnoreCase(DEFAULT_PAYMENT_MODE)) {
                driverService.updateDriverWallet(userId, paymentModeDto.getPaymentMode(),
                        rideResponseDto.getStatus(),rideResponseDto.getFare());
            }
            logger.info("Updated ride status for user with ID {}", userId);
            return ApiResponseDto.statusOk(paymentModeDto);
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found", userId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (DatabaseException e) {
            logger.error("Error updating ride status for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error updating payment mode", e);
        }
    }

    @PatchMapping("/me/cancel")
    public ApiResponseDto<CancelRideResponseDto> cancelRide(@RequestBody CancelRideRequestDto cancelRideRequestDto) {
        String userId = JwtDecoder.extractUserIdFromToken();
        CancelRideResponseDto cancelRideResponseDto = null;
        try {
            logger.info("Attempting to cancel ride for user ID: {} with request: {}",
                    userId, cancelRideRequestDto);
            cancelRideResponseDto = driverService.cancelRide(cancelRideRequestDto, userId);
            logger.info("Ride cancellation successful for user ID: {}", userId);
            return ApiResponseDto.statusOk(cancelRideResponseDto);
        } catch (DatabaseException e) {
            logger.error("Error occurred while cancelling ride for user ID: {}", userId, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(cancelRideResponseDto, e);
        }
    }

    /**
     * <p>
     * Deletes the authenticated driver using the user ID from the JWT token.
     * </p>
     *
     * @return {@link ApiResponseDto}
     */
    @DeleteMapping("/me")
    public ApiResponseDto<?> deleteDriverById() {
        String id = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Attempting to delete driver with ID: {}", id);
            userService.deleteById(id);
            logger.info("Successfully deleted driver with ID: {}", id);
            return ApiResponseDto.statusOk("Deleted successfully");
        } catch (DatabaseException e) {
            logger.error("Error occurred while deleting driver with ID: {}. Error: {}", id, e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(null, e);
        }
    }
}