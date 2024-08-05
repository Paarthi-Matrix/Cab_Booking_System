package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideStatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.RIDE_CANCELLED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideRequestServiceTest {
    private static final Logger logger = LogManager.getLogger(RideRequestServiceTest.class);

    @Mock
    private RideRequestRepository rideRequestRepository;

    @InjectMocks
    private RideRequestServiceImpl rideRequestService;

    @BeforeEach
    void setUp() {
        reset(rideRequestRepository);
    }

    @Test
    void testCancelRideSuccess() {
        String customerId = "asd123";
        RideRequest rideRequest = new RideRequest();
        rideRequest.setStatus("BOOKED");
        when(rideRequestRepository.findByCustomerId(customerId)).thenReturn(Optional.of(rideRequest));
        RideStatusDto rideStatusDto = rideRequestService.cancelRide(customerId);
        assertNotNull(rideStatusDto);
        assertEquals(RIDE_CANCELLED, rideStatusDto.getStatus());
    }

    @Test
    void testCancelRideDatabaseException() {
        String customerId = "asd123";
        when(rideRequestRepository.findByCustomerId(customerId)).thenThrow(new RuntimeException("Database error"));
        try {
            rideRequestService.cancelRide(customerId);
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }
        DatabaseException thrown = assertThrows(DatabaseException.class, () -> rideRequestService.cancelRide(customerId));
        assertTrue(thrown.getMessage().contains("Failed to cancel ride details. Customer ID: " + customerId));
        verify(rideRequestRepository, never()).save(any());
    }
}
