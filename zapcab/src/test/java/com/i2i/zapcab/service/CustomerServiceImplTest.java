package com.i2i.zapcab.service;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_CUSTOMER_TIRE;
import static com.i2i.zapcab.common.ZapCabConstant.RIDE_BOOKED;
import com.i2i.zapcab.dto.AssignedDriverDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.i2i.zapcab.dto.CheckVehicleAvailabilityDto;
import com.i2i.zapcab.dto.VehicleAvailabilityResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleLocationService vehicleLocationService;

    @Mock
    private RideRequestService rideRequestService;

    @Mock
    private RideService rideService;

    @Mock
    private DriverService driverService;

    @Mock
    private OtpService otpService;

    @Mock
    private FareCalculatorService fareCalculatorService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAvailableVehiclesWithFare_SamePickupAndDropPoint() {
        CheckVehicleAvailabilityDto checkVehicleAvailabilityDto = new CheckVehicleAvailabilityDto();
        checkVehicleAvailabilityDto.setPickupPoint("Guindy");
        checkVehicleAvailabilityDto.setDropPoint("Guindy");
        VehicleAvailabilityResponseDto response = customerService.getAvailableVehiclesWithFare(checkVehicleAvailabilityDto);
        assertNull(response);
    }
    @Test
    void testSaveRideRequest_Success() {
        String userId = "user123";
        RideRequestDto rideRequestDto = RideRequestDto.builder()
                .distance(10).pickupPoint("Guindy").dropPoint("Velachery")
                .dropTime("0.20 hrs").vehicleCategory("SUV").fare(350).build();
        Customer customer = Customer.builder().id("cust1234").user(new User()).build();
        when(customerRepository.findByUserId(userId)).thenReturn(customer);
        when(rideRequestService.saveRideRequest(customer, rideRequestDto)).thenReturn(true);
        boolean result = customerService.saveRideRequest(userId, rideRequestDto);
        assertTrue(result);
        verify(customerRepository, times(1)).findByUserId(userId);
        verify(rideRequestService, times(1)).saveRideRequest(customer, rideRequestDto);
    }
    @Test
    void testSaveRideRequest_ExceptionDuringSave() {
        String userId = "user123";
        RideRequestDto rideRequestDto = new RideRequestDto();
        Customer customer = new Customer();
        when(customerRepository.findByUserId(userId)).thenReturn(customer);
        when(rideRequestService.saveRideRequest(customer, rideRequestDto)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            customerService.saveRideRequest(userId, rideRequestDto);
        });
        assertEquals("Error Occurred while saving ride request", exception.getMessage());
        verify(customerRepository, times(1)).findByUserId(userId);
        verify(rideRequestService, times(1)).saveRideRequest(customer, rideRequestDto);
    }
    @Test
    void testSaveCustomer_Success() {
        User user = User.builder().id("e1234-abc01").name("Indra prasath").email("indraprasath.manickavel@ideas2it.com")
                .gender("male").mobileNumber("9791205342").password("indra@2000").dateOfBirth(LocalDate.parse("2000-10-21"))
                .build();
        Customer customer = Customer.builder().id("cust1234").user(user).build();
        customer.setUser(user);
        customerService.saveCustomer(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testSaveCustomer_ExceptionDuringSave() {
        User user = new User();
        user.setName("Indra prasath");
        Customer customer = new Customer();
        customer.setUser(user);
        doThrow(new RuntimeException("Database error")).when(customerRepository).save(customer);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            customerService.saveCustomer(customer);
        });
        assertEquals("Unexpected error occurred while saving customer: Indra prasath", exception.getMessage());
        verify(customerRepository, times(1)).save(customer);
    }
    @Test
    void testUpdateRideAndDriverRating_Success() {
        String rideId = "ride123";
        RideRatingDto ratings = new RideRatingDto();
        ratings.setRatings(5);
        when(rideService.updateRideRating(rideId, ratings)).thenReturn("driver123");
        when(driverService.updateDriverRating("driver123", 5)).thenReturn(true);
        boolean result = customerService.updateRideAndDriverRating(rideId, ratings);
        assertTrue(result);
        verify(rideService, times(1)).updateRideRating(rideId, ratings);
        verify(driverService, times(1)).updateDriverRating("driver123", 5);
    }
    @Test
    void testUpdateRideAndDriverRating_ExceptionDuringUpdate() {
        String rideId = "ride123";
        RideRatingDto ratings = new RideRatingDto();
        ratings.setRatings(5);
        when(rideService.updateRideRating(rideId, ratings)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            customerService.updateRideAndDriverRating(rideId, ratings);
        });
        assertEquals("Error Occurred while Updating Ride And Driver Ratings", exception.getMessage());
        verify(rideService, times(1)).updateRideRating(rideId, ratings);
        verify(driverService, times(0)).updateDriverRating(anyString(), anyInt());
    }
    @Test
    void testGetAssignedDriverDetails_Success() {
        String customerId = "customer123";
        RideRequest rideRequest = RideRequest.builder().id("ride002").pickupPoint("Airport").dropPoint("Guindy")
                .distance(10).vehicleCategory("XUV").customer(Customer.builder().id("e1234-abcdef-100")
                        .tier(INITIAL_CUSTOMER_TIRE).build()).fare(450).build();
        Ride ride = Ride.builder().id("r005").rideRequest(rideRequest).status(RIDE_BOOKED).distance(10)
                .fare(450).dropPoint("Guindy").driver(any()).startTime(LocalDateTime.now()).build();
        Driver driver = new Driver();
        User driverUser = new User();
        driverUser.setName("John Doe");
        driverUser.setMobileNumber("1234567890");
        driver.setUser(driverUser);
        driver.setRatings(4);
        Vehicle vehicle = new Vehicle();
        vehicle.setCategory("SUV");
        vehicle.setModel("Toyota");
        vehicle.setLicensePlate("ABC123");
        driver.setVehicle(vehicle);
        ride.setDriver(driver);
        when(rideRequestService.checkStatusAssignedOrNot(customerId)).thenReturn(rideRequest);
        when(rideService.getRideByRideRequest(rideRequest.getId())).thenReturn(ride);
        when(otpService.generateOTP(customerId)).thenReturn("123456");
        AssignedDriverDto result = customerService.getAssignedDriverDetails(customerId);
        assertNotNull(result);
        assertEquals("123456", result.getOtp());
        assertEquals("John Doe", result.getName());
        assertEquals("1234567890", result.getMobileNumber());
        assertEquals(4, result.getRatings());
        assertEquals("SUV", result.getCategory());
        assertEquals("Toyota", result.getModel());
        assertEquals("ABC123", result.getLicensePlate());
        verify(rideRequestService, times(1)).checkStatusAssignedOrNot(customerId);
        verify(rideService, times(1)).getRideByRideRequest(rideRequest.getId());
        verify(otpService, times(1)).generateOTP(customerId);
    }
    @Test
    void testGetAssignedDriverDetails_NoRideRequestFound() {
        String customerId = "customer123";
        when(rideRequestService.checkStatusAssignedOrNot(customerId)).thenReturn(null);
        AssignedDriverDto result = customerService.getAssignedDriverDetails(customerId);
        assertNull(result);
        verify(rideRequestService, times(1)).checkStatusAssignedOrNot(customerId);
        verify(rideService, times(0)).getRideByRideRequest(anyString());
        verify(otpService, times(0)).generateOTP(anyString());
    }
    @Test
    void testGetAssignedDriverDetails_NoRideFound() {
        String customerId = "customer123";
        RideRequest rideRequest = new RideRequest();
        rideRequest.setId("rideRequest123");
        when(rideRequestService.checkStatusAssignedOrNot(customerId)).thenReturn(rideRequest);
        when(rideService.getRideByRideRequest(rideRequest.getId())).thenReturn(null);
        AssignedDriverDto result = customerService.getAssignedDriverDetails(customerId);
        assertNull(result);
        verify(rideRequestService, times(1)).checkStatusAssignedOrNot(customerId);
        verify(rideService, times(1)).getRideByRideRequest(rideRequest.getId());
        verify(otpService, times(0)).generateOTP(anyString());
    }
    @Test
    void testGetAssignedDriverDetails_ExceptionDuringDataRetrieval() {
        String customerId = "customer123";
        when(rideRequestService.checkStatusAssignedOrNot(customerId)).thenThrow(new RuntimeException("Database error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            customerService.getAssignedDriverDetails(customerId);
        });
        assertEquals("Error occurred while fetching assigned driver for the customer with ID: " + customerId, exception.getMessage());
        verify(rideRequestService, times(1)).checkStatusAssignedOrNot(customerId);
        verify(rideService, times(0)).getRideByRideRequest(anyString());
        verify(otpService, times(0)).generateOTP(anyString());
    }

}
