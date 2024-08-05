package com.i2i.zapcab.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.UpdateRideDto;
import com.i2i.zapcab.dto.UpdateRideResponseDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.service.CustomerService;
import com.i2i.zapcab.service.RideRequestService;
import com.i2i.zapcab.service.UserService;
import com.i2i.zapcab.helper.JwtDecoder;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private RideRequestService rideRequestService;

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
        jwtDecoderMock.reset();
    }
    @Test
    void testGetAvailableVehiclesWithFare_Success() {
        CheckVehicleAvailabilityDto checkVehicleAvailabilityDto = new CheckVehicleAvailabilityDto();
        checkVehicleAvailabilityDto.setPickupPoint("Guindy");
        checkVehicleAvailabilityDto.setDropPoint("Velachery");
        VehicleAvailabilityResponseDto vehicleAvailabilityResponseDto = VehicleAvailabilityResponseDto.builder().pickup("Guindy")
                .drop("Velachery").rideRequestResponseDtos(any()).build();
        when(customerService.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto))
                .thenReturn(vehicleAvailabilityResponseDto);
        ApiResponseDto<?> response = customerController.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vehicleAvailabilityResponseDto, response.getData());
    }
    @Test
    void testGetAvailableVehiclesWithFare_SamePickupAndDropPoint() {
        CheckVehicleAvailabilityDto checkVehicleAvailabilityDto = new CheckVehicleAvailabilityDto();
        checkVehicleAvailabilityDto.setPickupPoint("Airport");
        checkVehicleAvailabilityDto.setDropPoint("Airport");
        when(customerService.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto)).thenReturn(null);
        ApiResponseDto<?> response = customerController.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Same pickup and drop point not allowed", response.getData());
    }
    @Test
    void testGetAvailableVehiclesWithFare_DatabaseException() {
        CheckVehicleAvailabilityDto checkVehicleAvailabilityDto = new CheckVehicleAvailabilityDto();
        checkVehicleAvailabilityDto.setPickupPoint("Guindy");
        checkVehicleAvailabilityDto.setDropPoint("Airport");
        when(customerService.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto))
                .thenThrow(new DatabaseException("Error"));
        ApiResponseDto<?> response = customerController.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
    @Test
    void testSaveRideRequest_Success() {
        RideRequestDto rideRequestDto = RideRequestDto.builder()
                .distance(10).pickupPoint("Guindy").dropPoint("Velachery")
                .dropTime("0.20 hrs").vehicleCategory("SUV").fare(350).build();
        String userId = "e123-abc";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.saveRideRequest(userId, rideRequestDto)).thenReturn(true);
        ApiResponseDto<String> response = customerController.saveRideRequest(rideRequestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Searching For Captain to Accept...", response.getData());
    }
    @Test
    void testSaveRideRequest_NotFound() {
        RideRequestDto rideRequestDto = RideRequestDto.builder()
                .distance(10).pickupPoint("Guindy").dropPoint("Velachery")
                .dropTime("0.20 hrs").vehicleCategory("SUV").fare(350).build();
        String userId = "e12-abc";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.saveRideRequest(userId, rideRequestDto)).thenReturn(false);
        ApiResponseDto<String> response = customerController.saveRideRequest(rideRequestDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getData());
    }
    @Test
    void testSaveRideRequest_DatabaseException() {
        RideRequestDto rideRequestDto = new RideRequestDto();
        rideRequestDto.setPickupPoint("Guindy");
        rideRequestDto.setDropPoint("Velachery");
        rideRequestDto.setDistance(10);
        rideRequestDto.setFare(350);
        String userId = "user123";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.saveRideRequest(userId, rideRequestDto)).thenThrow(new DatabaseException("Error"));
        ApiResponseDto<String> response = customerController.saveRideRequest(rideRequestDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
    @Test
    void testUpdateRideAndDriverRating_Success() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        rideRatingDto.setRatings(5);
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenReturn(true);

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ratings updated successfully", response.getData());
    }

    @Test
    void testUpdateRideAndDriverRating_NotFound() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        rideRatingDto.setRatings(2);
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenReturn(false);

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testUpdateRideAndDriverRating_DatabaseException() {
        RideRatingDto rideRatingDto = new RideRatingDto();
        rideRatingDto.setRatings(3);
        String rideId = "ride123";
        when(customerService.updateRideAndDriverRating(rideId, rideRatingDto)).thenThrow(new DatabaseException("Database error"));

        ApiResponseDto<String> response = customerController.updateRideAndDriverRating(rideId, rideRatingDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testUpdateRideDetails_Success() {
        UpdateRideDto updateRideDto = new UpdateRideDto();
        updateRideDto.setPickupPoint("Airport");
        updateRideDto.setDropPoint("Guindy");
        updateRideDto.setVehicleCategory("SUV");
        String userId = "u1234-xyz";
        String customerId = "c001-asd";
        UpdateRideResponseDto updateRideResponseDto = UpdateRideResponseDto.builder()
                .pickupPoint("Airport").dropPoint("Guindy").distance(10)
                .fare(200).build();
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
        updateRideDto.setPickupPoint("Guindy");
        updateRideDto.setDropPoint("Velachery");
        updateRideDto.setVehicleCategory("SUV");
        String userId = "u1234";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.retrieveCustomerIdByUserId(userId)).thenThrow(new NotFoundException("Not found"));
        ApiResponseDto<?> response = customerController.updateRideDetails(updateRideDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getData());
    }

    @Test
    void testUpdateRideDetails_DatabaseException() {
        UpdateRideDto updateRideDto = new UpdateRideDto();
        updateRideDto.setPickupPoint("Guindy");
        updateRideDto.setDropPoint("Airport");
        updateRideDto.setVehicleCategory("SUV");
        String userId = "u12-abc";
        String customerId = "cust1234";
        when(JwtDecoder.extractUserIdFromToken()).thenReturn(userId);
        when(customerService.retrieveCustomerIdByUserId(userId)).thenReturn(customerId);
        when(rideRequestService.updateRideDetails(customerId, updateRideDto)).thenThrow(new DatabaseException("Database error"));
        ApiResponseDto<?> response = customerController.updateRideDetails(updateRideDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getData());
    }
}
