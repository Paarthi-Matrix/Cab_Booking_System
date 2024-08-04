package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @InjectMocks
    private RideServiceImpl rideServiceImpl;

    @Mock
    private RideRepository rideRepository;

    @BeforeEach
    void setUp() {
        reset(rideRepository);
    }

    @Test
    public void testUpdateRideRating() {
        String rideId = "asd123";
        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setRideRating(0);
        Driver driver = new Driver();
        driver.setId("driver123");
        ride.setDriver(driver);
        RideRatingDto rideRatingDto = new RideRatingDto();
        rideRatingDto.setRatings(4);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(ride);
        String driverId = rideServiceImpl.updateRideRating(rideId, rideRatingDto);
        assertEquals(driver.getId(), driverId);
        assertEquals(4, ride.getRideRating());
    }

//    @Test
//    public void testUpdateRideRating_NotFoundException() {
//        String rideId = "asd123";
//        RideRatingDto rideRatingDto = new RideRatingDto();
//        rideRatingDto.setRatings(4);
//
//        when(rideRepository.findById(rideId)).thenReturn(Optional.empty());
//        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
//            rideServiceImpl.updateRideRating(rideId, rideRatingDto);
//        });
//
//        assertEquals("Ride not found for ID : " + rideId, thrown.getMessage());
//    }

    @Test
    public void testUpdateRideRatingDatabaseException() {
        String rideId = "asd123";
        RideRatingDto rideRatingDto = new RideRatingDto();
        rideRatingDto.setRatings(4);
        when(rideRepository.findById(rideId)).thenThrow(new RuntimeException("Database error"));
        DatabaseException thrown = assertThrows(DatabaseException.class, () -> {
            rideServiceImpl.updateRideRating(rideId, rideRatingDto);
        });
        assertEquals("Error Occurred while updating ride rating with its id: " + rideId, thrown.getMessage());
    }
}
