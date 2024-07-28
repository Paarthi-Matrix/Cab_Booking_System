package com.i2i.zapcab.controller;

import java.util.List;

import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.dto.MaskMobileNumberRequestDto;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.dto.OTPResponseDto;
import com.i2i.zapcab.dto.OtpRequestDto;

import com.i2i.zapcab.service.AuthenticationServiceImpl;
import com.i2i.zapcab.service.RideService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private DriverService driverService;
    @Autowired
    private RideService rideService;

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
    @PatchMapping("/me/locations")
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
    public ApiResponseDto<List<RequestedRideDto>> getAvailableDrivers(@RequestBody GetRideRequestListsDto getRideRequestListsDto ){
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
        } catch (UnexpectedException e) {
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
    @PostMapping("/me/request/accept")
    public ApiResponseDto<RideDetailsDto> getRideDetails(@RequestBody DriverSelectedRideDto selectedRideDto) {
        RideDetailsDto rideDetailsDto = null;
        try {
            logger.info("Received request to get ride details for: {}", selectedRideDto);
            rideDetailsDto = driverService.getRideDetails(selectedRideDto);
            logger.info("Successfully retrieved ride details: {}", rideDetailsDto);
            return ApiResponseDto.statusOk(rideDetailsDto);
        } catch (UnexpectedException e) {
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
        } catch (UnexpectedException e) {
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
        OTPResponseDto otpResponseDto = null;
        try {
           if(driverService.otpValidation(otpRequestDto)) {
               otpResponseDto = OTPResponseDto.builder().msg("Otp validated successfully ! Your ride has been started").build();
               return ApiResponseDto.statusOk(otpResponseDto);
           } else {
               otpResponseDto = OTPResponseDto.builder().msg("Invalid otp !!!").build();
              return ApiResponseDto.statusOk(otpResponseDto);
           }
        } catch (UnexpectedException e) {
            return ApiResponseDto.statusInternalServerError(otpResponseDto, e);
        }
    }

    @PatchMapping("/{id}/paymentmodes")
    public ApiResponseDto<?> paymentMode(@PathVariable String id, @RequestBody PaymentModeDto paymentModeDto) {
        try {
            PaymentModeDto paymentMode = rideService.paymentMode(id, paymentModeDto);
            logger.info("Updated payment mode for ride with ID {}", id);
            return ApiResponseDto.statusOk(paymentMode);
        } catch (NotFoundException e) {
            logger.warn("Ride with ID {} not found", id);
            return ApiResponseDto.statusNotFound("Invalid ID");
        } catch (UnexpectedException e) {
            logger.error("Error updating ride status for ride with ID {}", id, e);
            return ApiResponseDto.statusInternalServerError("Error updating payment mode", e);
        }
    }
}
