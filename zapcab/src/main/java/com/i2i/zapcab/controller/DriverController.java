package com.i2i.zapcab.controller;

import java.util.List;

import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OTPResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;

import com.i2i.zapcab.service.AuthenticationServiceImpl;
import com.i2i.zapcab.service.RideService;
import com.i2i.zapcab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.ChangePasswordRequestDto;
import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.DriverService;

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

    /**
     * <p>
     *     This function is used to update the status and location of the driver.
     *     <p>
     *         NOTE : The status of the driver can be as follows,
     *     </p>
     *     <ol>
     *         <li>ONDUTY</li>
     *         <li>OFFDUTY</li>
     *         <li>SUSPENDED</li>
     *     </ol>
     * </p>
     *
     * @param updateDriverStatusDto {@link UpdateDriverStatusDto}
     *       This must contain all the values of the `UpdateDriverStatusDto`.
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me")
    public ApiResponseDto<String> updateDriverStatusAndLocation(@RequestBody UpdateDriverStatusDto updateDriverStatusDto) {
        logger.debug("Entering into the method to update the driver status and location...");
        String Userid = JwtDecoder.extractUserIdFromToken();
        try {
            logger.info("Updating the status and location for the driver {} ",Userid);
            driverService.updateDriverStatusAndLocation(Userid,updateDriverStatusDto);
        } catch (NotFoundException e) {
            logger.error("Unale to update the details for the driver :{} ", Userid);
            return ApiResponseDto.statusNoContent("No such driver available");
        }
        logger.debug("Exiting the method ");
        return ApiResponseDto.statusOk("Driver location and status updated successfully!");
    }

    /**
     *<p>
     *     This method is used to display the requested ride to the driver. The filtration is done based on
     *      location and the vehicle category.
     *</p>
     * @param getRideRequestListsDto {@link GetRideRequestListsDto}
     * @return ApiResponseDto<List<RequestedRideDto> {@link RequestedRideDto}
     */
   @GetMapping("/me/requests")
    public ApiResponseDto<List<RequestedRideDto>> getAvailableRideRequest(@RequestBody GetRideRequestListsDto getRideRequestListsDto) {
       List<RequestedRideDto> requestedRideDtos = null;
       try {
           requestedRideDtos = driverService.getRideRequests(getRideRequestListsDto);
           return ApiResponseDto.statusOk(requestedRideDtos);
       } catch (NotFoundException e) {
           return ApiResponseDto.statusNotFound(requestedRideDtos, e);
       }
   }

    /**
     * <p>
     *     This method is responsible for changing the password of the driver.
     * </p>
     * @param changePasswordRequestDto {@link ChangePasswordRequestDto}
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me/password")
    public ApiResponseDto<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            String id = JwtDecoder.extractUserIdFromToken();
            driverService.changePassword(id, changePasswordRequestDto.getNewPassword());
        } catch (DatabaseException e) {
            return ApiResponseDto.statusInternalServerError("Unexpected error occurred while changing password", e);
        }
        return ApiResponseDto.statusOk("Driver password changed successfully!");
    }

    /**
     * <p>
     *     This method is used to fetch the ride details mapped with the customer.
     * </p>
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return RideDetailsDto
     *          Holds the customer requested ride data {@link RideDetailsDto}
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

    @PatchMapping("/me/mask")
    public ApiResponseDto<MaskMobileNumberResponseDto> maskMobileNumber(@RequestBody MaskMobileNumberRequestDto maskMobileNumberRequestDto) {
        MaskMobileNumberResponseDto maskMobileNumberResponseDto = null;
        try {
            String id = JwtDecoder.extractUserIdFromToken();
            return ApiResponseDto.statusOk(driverService.updateMaskMobileNumber(id,maskMobileNumberRequestDto));
        } catch (DatabaseException e) {
            return ApiResponseDto.statusInternalServerError(maskMobileNumberResponseDto, e);
        }
    }

    /**
     * <p>
     *     Validated the otp provided by the customer to start the ride.
     * </p>
     * @param otpRequestDto {@link OtpRequestDto}
     * @return OTPResponseDto
     *        provides the successfull message if the otp is correct otherwise it returns an invalid message
     */
    @PostMapping("/me/otp")
    ApiResponseDto<OTPResponseDto> otpValidation(@RequestBody OtpRequestDto otpRequestDto) {
        String id =JwtDecoder.extractUserIdFromToken();
        OTPResponseDto otpResponseDto = null;
        try {
           if(driverService.otpValidation(otpRequestDto,id)) {
               otpResponseDto = OTPResponseDto.builder().msg("Otp validated successfully ! Your ride has been started").build();
               return ApiResponseDto.statusOk(otpResponseDto);
           } else {
               otpResponseDto = OTPResponseDto.builder().msg("Invalid otp !!!").build();
              return ApiResponseDto.statusOk(otpResponseDto);
           }
        } catch (DatabaseException e) {
            return ApiResponseDto.statusInternalServerError(otpResponseDto, e);
        }
    }

    /**
     * <p>
     *     This method handles updating the payment mode for a driver and subsequently updating the ride status.
     * </p>
     *
     * @param paymentModeDto {@link PaymentModeDto}
     * @return ApiResponseDto<paymentMode>
     *     The response entity containing the updated payment mode, or an error message.
     */
    @PatchMapping("/me/payments")
    public ApiResponseDto<?> updatePaymentMode(@RequestBody PaymentModeDto paymentModeDto) {
        String userId = JwtDecoder.extractUserIdFromToken();
        try {
            String driverId = driverService.retrieveDriverIdByUserId(userId);
            PaymentModeDto paymentMode = rideService.paymentMode(driverId, paymentModeDto);
            logger.info("Updated payment mode for user with ID {}", userId);
            rideService.updateRideStatus(driverId);
            logger.info("Updated ride status for user with ID {}", userId);
            return ApiResponseDto.statusOk(paymentMode);
        } catch (NotFoundException e) {
            logger.warn("User with ID {} not found", userId);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (DatabaseException e) {
            logger.error("Error updating ride status for user with ID {}", userId, e);
            return ApiResponseDto.statusInternalServerError("Error updating payment mode", e);
        }
    }

    /**
     * <p>
     * Deletes the authenticated driver using the user ID from the JWT token.
     * </p>
     *
     * @return {@link ApiResponseDto}
     */
    @DeleteMapping("me")
    public ApiResponseDto<?> deleteDriverById(){
        String id = JwtDecoder.extractUserIdFromToken();
        try{
            userService.deleteById(id);
            return ApiResponseDto.statusOk("Deleted successfully");
        }catch (DatabaseException e){
            logger.error(e.getMessage(),e);
            return ApiResponseDto.statusInternalServerError(null,e);
        }
    }
}