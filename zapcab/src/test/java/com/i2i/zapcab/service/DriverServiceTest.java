package com.i2i.zapcab.service;
import com.i2i.zapcab.dto.CancelRideRequestDto;
import com.i2i.zapcab.dto.CancelRideResponseDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;
import com.i2i.zapcab.repository.DriverRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.i2i.zapcab.common.ZapCabConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class DriverServiceTest {

    @InjectMocks
    private DriverServiceImpl driverService;
    @Mock
    DriverRepository driverRepository;
    @Mock
    private RideService rideService;
    @Mock
    UserService userService;
    @Mock
    private VehicleService vehicleService;
    @Mock
    private VehicleLocationService vehicleLocationService;

    private Ride ride;
    private Driver driver;
    @Test
    public void testSaveDriver() {
        Driver driver = Driver.builder().id("driver123").status("OFF-DUTY").vehicle(Vehicle.builder()
                .status(VEHICLE_AVAILABLE_STATUS).id("vehicle123").vehicleLocation(VehicleLocation.builder()
                        .location("Guindy").build()).build()).build();
        when(driverRepository.save(driver)).thenReturn(driver);
        Driver savedDriver = driverService.saveDriver(driver);
        assertNotNull(savedDriver);
        assertEquals("driver123", savedDriver.getId());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void testSaveDriver_DatabaseException() {
        Driver driver = Driver.builder().status("OFF-DUTY").user(User.builder().name("Meenakshi").build()).vehicle(Vehicle.builder()
                .status(VEHICLE_AVAILABLE_STATUS).id("vehicle123").vehicleLocation(VehicleLocation.builder()
                        .location("Guindy").build()).build()).build();
        when(driverRepository.save(driver)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            driverService.saveDriver(driver);
        });
        assertEquals("Error occurred while saving the driver Meenakshi", exception.getMessage());
        verify(driverRepository, times(1)).save(driver);
    }
    @Test
    void testUpdateDriverStatusAndLocation_Success() {
        User user = User.builder().id("user1234").name("Sriram").email("sriram.baskaran@ideas2it.com")
                .build();
        Driver driver1 = Driver.builder().status("OFF-DUTY").user(user).vehicle(Vehicle.builder()
                .status(VEHICLE_AVAILABLE_STATUS).id("vehicle123").vehicleLocation(VehicleLocation.builder()
                        .location("Guindy").build()).build()).build();
        UpdateDriverStatusDto updateDriverStatusDto = new UpdateDriverStatusDto();
        updateDriverStatusDto.setStatus(DRIVER_STATUS);
        updateDriverStatusDto.setLocation("Velachery");
        when(userService.getUserById("user1234")).thenReturn(Optional.of(user));
        when(driverRepository.findByUserId("user1234")).thenReturn(driver1);
        driverService.updateDriverStatusAndLocation("user1234", updateDriverStatusDto);
        verify(vehicleService, times(1)).updateVehicleStatus(VEHICLE_AVAILABLE_STATUS, driver1.getVehicle());
        verify(vehicleLocationService, times(1)).updateVehicleLocationByVehicleId("Velachery", driver1.getVehicle().getId());
        assertEquals(DRIVER_STATUS, driver1.getStatus());
    }

    @Test
    void testUpdateDriverStatusAndLocation_UserNotFound() {
        UpdateDriverStatusDto updateDriverStatusDto = new UpdateDriverStatusDto();
        when(userService.getUserById("user123")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            driverService.updateDriverStatusAndLocation("user123", updateDriverStatusDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(vehicleService, never()).updateVehicleStatus(anyString(), any());
        verify(vehicleLocationService, never()).updateVehicleLocationByVehicleId(anyString(), any());
    }

    @Test
    void testUpdateDriverStatusAndLocation_DriverNotFound() {
        UpdateDriverStatusDto updateDriverStatusDto = new UpdateDriverStatusDto();
        updateDriverStatusDto.setStatus("ONDUTY");
        updateDriverStatusDto.setLocation("New Location");
        when(userService.getUserById("user123")).thenReturn(Optional.of(new User()));
        when(driverRepository.findByUserId("user123")).thenReturn(null);
        driverService.updateDriverStatusAndLocation("user123", updateDriverStatusDto);
        verify(vehicleService, never()).updateVehicleStatus(anyString(), any());
        verify(vehicleLocationService, never()).updateVehicleLocationByVehicleId(anyString(), any());
    }

    @Test
    void testCancelRide_Success() {
        Driver driver1 = Driver.builder().id("driver123").status(DRIVER_STATUS).noOfCancellation(1).vehicle(Vehicle.builder()
                .status(VEHICLE_AVAILABLE_STATUS).id("vehicle123").vehicleLocation(VehicleLocation.builder()
                        .location("Guindy").build()).build()).build();
        Ride ride = Ride.builder().id("Rid123").rideRequest(any()).status("cancel").build();
        CancelRideRequestDto cancelRideRequestDto = new CancelRideRequestDto();
        cancelRideRequestDto.setRideId("Rid123");
        cancelRideRequestDto.setCancel("cancel");
        when(rideService.getRideById("Rid123")).thenReturn(Optional.of(ride));
        when(driverRepository.findByUserId("user1234")).thenReturn(driver1);
        CancelRideResponseDto response = driverService.cancelRide(cancelRideRequestDto, "user1234");
        assertNotNull(response);
        assertEquals("You have only one cancellation more !!", response.getMessage());
        assertEquals("cancel", ride.getStatus());
        assertEquals(0, driver1.getNoOfCancellation());
        verify(rideService, times(1)).getRideById("Rid123");
        verify(driverRepository, times(1)).findByUserId("user1234");
    }

    @Test
    void testCancelRide_DriverSuspended() {
        Driver driver = Driver.builder().id("driver123").status(DRIVER_STATUS).noOfCancellation(0).vehicle(Vehicle.builder()
                .status(VEHICLE_AVAILABLE_STATUS).id("vehicle123").vehicleLocation(VehicleLocation.builder()
                        .location("Guindy").build()).build()).build();
        Ride ride = Ride.builder().id("Rid1234").rideRequest(any()).status("cancel").build();
        CancelRideRequestDto cancelRideRequestDto = new CancelRideRequestDto();
        cancelRideRequestDto.setRideId("Rid1234");
        when(rideService.getRideById("Rid1234")).thenReturn(Optional.of(ride));
        when(driverRepository.findByUserId("user1234")).thenReturn(driver);
        CancelRideResponseDto response = driverService.cancelRide(cancelRideRequestDto, "user1234");
        assertNotNull(response);
        assertEquals("You have only one cancellation more !!", response.getMessage());
        assertEquals("cancel", ride.getStatus());
        assertEquals(-1, driver.getNoOfCancellation());
        assertEquals("TEMPORARILY_SUSPENDED", driver.getStatus());
        verify(rideService, times(1)).getRideById("Rid1234");
        verify(driverRepository, times(1)).findByUserId("user1234");
    }
    @Test
    void testRetrieveDriverIdByUserId_Success() {
        String userId = "user123";
        Driver driver = new Driver();
        driver.setId("driver123");
        when(driverRepository.findByUserId(userId)).thenReturn(driver);
        String driverId = driverService.retrieveDriverIdByUserId(userId);
        assertEquals("driver123", driverId);
    }
    @Test
    void testRetrieveDriverIdByUserId_Exception() {
        String userId = "user123";
        when(driverRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            driverService.retrieveDriverIdByUserId(userId);
        });
        assertEquals("Failed to retrieve the driver ID for user ID: user123", exception.getMessage());
    }
    @Test
    void testUpdateDriverWallet_SuccessWithNonZeroWallet() {
        String id = "user123";
        String paymentMode = PAYMENT_CASH;
        String rideStatus = RIDE_COMPLETED;
        int fare = 100;
        Driver driver = new Driver();
        driver.setId("driver1234");
        driver.setWallet(480);
        when(driverRepository.findByUserId(id)).thenReturn(driver);
        driverService.updateDriverWallet(id, paymentMode, rideStatus, fare);
        assertEquals(480, driver.getWallet()); // 500 - 20% of 100
    }

    @Test
    void testUpdateDriverWallet_SuccessWithZeroWallet() {
        String id = "driver123";
        String paymentMode = PAYMENT_CASH;
        String rideStatus = RIDE_COMPLETED;
        int fare = 100;
        Driver driver = new Driver();
        driver.setId(id);
        driver.setWallet(0);
        driver.setVehicle(new Vehicle());
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        driverService.updateDriverWallet(id, paymentMode, rideStatus, fare);
        verify(driverRepository).save(driver);
        assertEquals(TEMPORARILY_UNAVAILABLE, driver.getStatus());
        assertEquals(VEHICLE_STATUS_UNAVAILABLE, driver.getVehicle().getStatus());
    }

    @Test
    void testUpdateDriverWallet_PaymentModeOrRideStatusNotMatch() {
        String id = "user123";
        String paymentMode = PAYMENT_CASH;
        String rideStatus = RIDE_COMPLETED;
        int fare = 100;
        Driver driver = new Driver();
        driver.setId("driver1234");
        when(driverRepository.findByUserId(id)).thenReturn(driver);
        driverService.updateDriverWallet(id, paymentMode, rideStatus, fare);
        verify(driverRepository, never()).save(driver);
    }

    @Test
    void testUpdateDriverWallet_DriverNotFound() {
        String id = "driver123";
        String paymentMode = PAYMENT_CASH;
        String rideStatus = RIDE_COMPLETED;
        int fare = 100;
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
        driverService.updateDriverWallet(id, paymentMode, rideStatus, fare);
        verify(driverRepository, never()).save(any(Driver.class));
    }
    @Test
    void testUpdateDriverWallet_Exception() {
        String id = "driver123";
        String paymentMode = PAYMENT_CASH;
        String rideStatus = RIDE_COMPLETED;
        int fare = 100;
        when(driverRepository.findById(id)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            driverService.updateDriverWallet(id, paymentMode, rideStatus, fare);
        });
        assertEquals("Unable to update the wallet for the driver: " + id, exception.getMessage());
    }
}


