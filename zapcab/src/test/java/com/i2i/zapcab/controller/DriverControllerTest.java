package com.i2i.zapcab.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.service.VehicleLocationService;
import org.junit.jupiter.api.AfterAll;

import static com.i2i.zapcab.common.ZapCabConstant.RIDE_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static com.i2i.zapcab.common.ZapCabConstant.PAYMENT_CASH;

import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.DriverService;
import com.i2i.zapcab.service.RideService;

@ExtendWith(MockitoExtension.class)
public class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @Mock
    private RideService rideService;

    @Mock
    private VehicleLocationService vehicleLocationService;

    @InjectMocks
    private DriverController driverController;

    private static MockedStatic<JwtDecoder> jwtDecoderMock;

    @BeforeAll
    static void init() {
        jwtDecoderMock = mockStatic(JwtDecoder.class);
    }

    @AfterAll
    static void tearDown() {
        jwtDecoderMock.close();
    }

    @BeforeEach
    void setUp() {
        jwtDecoderMock.reset(); // Reset static mocking before each test
    }

    @Test
    void testUpdateDriverStatusAndLocation_Success() {
        UpdateDriverStatusDto updateDriverStatusDto = new UpdateDriverStatusDto();
        updateDriverStatusDto.setLocation("Velchery");
        updateDriverStatusDto.setStatus("Available");
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        ApiResponseDto<String> response = driverController.updateDriverStatusAndLocation(updateDriverStatusDto);
        verify(driverService).updateDriverStatusAndLocation(userId, updateDriverStatusDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Driver location and status updated successfully!", response.getData());
    }

    @Test
    void testUpdateDriverStatusAndLocation_NotFound() {
        UpdateDriverStatusDto updateDriverStatusDto = UpdateDriverStatusDto.builder().location("Airport")
                .status("UnAvailable").build();
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        doThrow(new NotFoundException("No such driver available")).when(driverService).updateDriverStatusAndLocation(userId, updateDriverStatusDto);
        ApiResponseDto<String> response = driverController.updateDriverStatusAndLocation(updateDriverStatusDto);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No such driver available", response.getData());
    }


    @Test
    void testGetAvailableRideRequest_NotFound() {
        GetRideRequestListsDto getRideRequestListsDto = GetRideRequestListsDto.builder().category("SUV").location("Velachery").build();
        when(driverService.getRideRequests(any())).thenReturn(Collections.emptyList());
        ApiResponseDto<List<RequestedRideDto>> response = driverController.getAvailableRideRequest(getRideRequestListsDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getData());

    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setNewPassword("abc123");
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        ApiResponseDto<String> response = driverController.changePassword(changePasswordRequestDto);
        verify(driverService).changePassword(userId, changePasswordRequestDto.getNewPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Driver password changed successfully!", response.getData());
    }

    @Test
    void testChangePassword_DatabaseException() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setNewPassword("abc123");
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        doThrow(new DatabaseException("Database error")).when(driverService).changePassword(userId,
                changePasswordRequestDto.getNewPassword());
        ApiResponseDto<String> response = driverController.changePassword(changePasswordRequestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
    @Test
    void testAcceptRide_Success() {
        DriverSelectedRideDto selectedRideDto = new DriverSelectedRideDto();
        selectedRideDto.setRideId("RID1234");
        selectedRideDto.setCustomerName("Meenakshi");
        selectedRideDto.setMobileNumber("9008017243");
        RideDetailsDto rideDetailsDto = RideDetailsDto.builder()
                .pickupPoint("Guindy").distance(10).customerName("Meenakshi").mobileNumber("9008017243").build();
        String userId = "driver123";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(driverService.acceptRide(selectedRideDto, userId)).thenReturn(rideDetailsDto);
        ApiResponseDto<RideDetailsDto> response = driverController.acceptRide(selectedRideDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideDetailsDto, response.getData());
    }

    @Test
    void testAcceptRide_DatabaseException() {
        DriverSelectedRideDto selectedRideDto = new DriverSelectedRideDto();
        selectedRideDto.setMobileNumber("8925456206");
        selectedRideDto.setCustomerName("Sriram");
        String userId = "driver123";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        doThrow(new DatabaseException("Error")).when(driverService).acceptRide(selectedRideDto, userId);
        ApiResponseDto<RideDetailsDto> response = driverController.acceptRide(selectedRideDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testOtpValidation_Success() {
        OtpRequestDto otpRequestDto = new OtpRequestDto();
        otpRequestDto.setOtp("123456");
        OTPResponseDto otpResponseDto = OTPResponseDto.builder().msg("Otp validated successfully! Your ride has been started").build();
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(Mockito.any());
        when(driverService.otpValidation(Mockito.any(), userId)).thenReturn(true);
        ApiResponseDto<OTPResponseDto> response = driverController.otpValidation(otpRequestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(otpResponseDto.getMsg(), response.getData().getMsg());
    }

    @Test
    void testOtpValidation_InvalidOtp() {
        OtpRequestDto otpRequestDto = new OtpRequestDto();
        otpRequestDto.setCustomerMobileNumber("8925456206");
        otpRequestDto.setOtp("002341");
        OTPResponseDto otpResponseDto = OTPResponseDto.builder().msg("Invalid otp!!!").build();
        String userId = "driver123";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(driverService.otpValidation(otpRequestDto, userId)).thenReturn(false);
        ApiResponseDto<OTPResponseDto> response = driverController.otpValidation(otpRequestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(otpResponseDto.getMsg(), response.getData().getMsg());
    }

    @Test
    void testOtpValidation_DatabaseException() {
        OtpRequestDto otpRequestDto = new OtpRequestDto();
        otpRequestDto.setCustomerMobileNumber("8925456206");
        otpRequestDto.setOtp("891023");
        String userId = "driver123";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        doThrow(new DatabaseException("Database error")).when(driverService).otpValidation(otpRequestDto, userId);
        ApiResponseDto<OTPResponseDto> response = driverController.otpValidation(otpRequestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testUpdatePaymentMode_Success() {
        PaymentModeDto paymentModeDto = new PaymentModeDto();
        paymentModeDto.setPaymentMode(PAYMENT_CASH);
        RideResponseDto rideResponseDto = RideResponseDto.builder().fare(450.0).status(RIDE_COMPLETED).build();
        String userId = "user123";
        String driverId = "driver456";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(driverService.retrieveDriverIdByUserId(userId)).thenReturn(driverId);
        when(rideService.setPaymentMode(driverId, paymentModeDto)).thenReturn(rideResponseDto);
        when(rideService.updateRideStatus(driverId)).thenReturn("Velachery");
        when(driverService.getVehicleIdByDriverId(driverId)).thenReturn("vehicle002");
        vehicleLocationService.updateVehicleLocationByVehicleId("Velachery", "vehicle002");
        ApiResponseDto<?> response = driverController.updatePaymentMode(paymentModeDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentModeDto, response.getData());
    }

    @Test
    void testUpdatePaymentMode_NotFound() {
        PaymentModeDto paymentModeDto = new PaymentModeDto();
        paymentModeDto.setPaymentMode(PAYMENT_CASH);
        String userId = UUID.randomUUID().toString();
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        doThrow(new NotFoundException("Invalid ID")).when(driverService).retrieveDriverIdByUserId(userId);
        ApiResponseDto<?> response = driverController.updatePaymentMode(paymentModeDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getData());
    }

    @Test
    void testUpdatePaymentMode_DatabaseException() {
        PaymentModeDto paymentModeDto = new PaymentModeDto();
        paymentModeDto.setPaymentMode("Online");
        String userId = "User123";
        String driverId = "driver456";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(driverService.retrieveDriverIdByUserId(userId)).thenReturn(driverId);
        doThrow(new DatabaseException("Database error")).when(rideService).setPaymentMode(driverId, paymentModeDto);
        ApiResponseDto<?> response = driverController.updatePaymentMode(paymentModeDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
}
