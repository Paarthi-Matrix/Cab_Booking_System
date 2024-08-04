package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.service.CustomerService;
import com.i2i.zapcab.service.RideRequestService;
import com.i2i.zapcab.service.UserService;
import com.i2i.zapcab.helper.JwtDecoder;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private RideRequestService rideRequestService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerController customerController;

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
    void testUpdateRideAndDriverRating_Success() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenReturn(true);

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ratings updated successfully", response.getData());
    }

    @Test
    void testUpdateRideAndDriverRating_NotFound() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenReturn(false);

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testUpdateRideAndDriverRating_DatabaseException() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenThrow(new DatabaseException("Database error"));

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testUpdateRideDetails_Success() {
        UpdateRideDto updateRideDto = new UpdateRideDto();
        updateRideDto.setPickupPoint("123 Main St");
        updateRideDto.setDropPoint("456 Elm St");
        updateRideDto.setVehicleCategory("SUV");

        String userId = "user123";
        String customerId = "customer123";
        UpdateRideResponseDto updateRideResponseDto = new UpdateRideResponseDto();
        updateRideResponseDto.setPickupPoint("123 Main St");
        updateRideResponseDto.setDropPoint("456 Elm St");
        updateRideResponseDto.setVehicleCategory("SUV");

        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.retrieveCustomerIdByUserId(userId)).thenReturn(customerId);
        when(rideRequestService.updateRideDetails(customerId, updateRideDto)).thenReturn(updateRideResponseDto);

        ApiResponseDto<?> response = customerController.updateRideDetails(updateRideDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateRideResponseDto, response.getData());
    }

    @Test
    void testUpdateRideDetails_NotFound() {
        UpdateRideDto updateRideDto = new UpdateRideDto();
        updateRideDto.setPickupPoint("123 Main St");
        updateRideDto.setDropPoint("456 Elm St");
        updateRideDto.setVehicleCategory("SUV");

        String userId = "user123";

        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.retrieveCustomerIdByUserId(userId)).thenThrow(new NotFoundException("Not found"));

        ApiResponseDto<?> response = customerController.updateRideDetails(updateRideDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getData());
    }

    @Test
    void testUpdateRideDetails_DatabaseException() {
        UpdateRideDto updateRideDto = new UpdateRideDto();
        updateRideDto.setPickupPoint("123 Main St");
        updateRideDto.setDropPoint("456 Elm St");
        updateRideDto.setVehicleCategory("SUV");

        String userId = "user123";
        String customerId = "customer123";

        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.retrieveCustomerIdByUserId(userId)).thenReturn(customerId);
        when(rideRequestService.updateRideDetails(customerId, updateRideDto)).thenThrow(new DatabaseException("Database error"));

        ApiResponseDto<?> response = customerController.updateRideDetails(updateRideDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
}
