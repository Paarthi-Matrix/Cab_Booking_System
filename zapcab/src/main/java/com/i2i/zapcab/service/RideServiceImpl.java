package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.EmailInvoiceDto;
import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.mapper.RideMapper;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRepository;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.RIDE_COMPLETED;

/**
 * <p>
 *     A service class that implements {@link RideService}
 *     Manages all the business logic like updating the ride rating, ride status
 * </p>
 */
@Service
public class RideServiceImpl implements RideService{
    private static final Logger logger  = LogManager.getLogger(RideServiceImpl.class);
    private RideMapper rideMapper = new RideMapper();
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public void saveRide(RideRequest rideRequest, Driver driver) {
        try {
            logger.info("Saving the request {} to the ride table....",rideRequest.getPickupPoint());
            Ride ride = rideMapper.rideRequestToRide(rideRequest, driver);
            ride.setStartTime(LocalDateTime.now());
            rideRepository.save(ride);
            logger.info("Successfully ride has been saved {}",rideRequest.getPickupPoint());
        } catch(Exception e) {
            logger.error("Unable to save the ride requested by the customer : {}",
                    rideRequest.getCustomer().getUser().getName());
            throw new DatabaseException("Unable to save the ride for the request : "+ rideRequest.getId(), e);
        }
    }

    @Override
    public String updateRideRating(String id, RideRatingDto ratings) {
        try {
            Ride ride = rideRepository.findById(id).get();
            ride.setRideRating(ratings.getRatings());
            rideRepository.save(ride);
            return ride.getDriver().getId();
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while updating ride rating with its id: " + id, e);
        }
    }

    @Override
    public RideResponseDto updateRideStatus(String id, StatusDto statusDto) {
        logger.debug("Updating status of ride with ID: {} to new status: {}", id, statusDto.getStatus());
        try {
            Optional<Ride> rideOptional = rideRepository.findById(id);
            if (!rideOptional.isPresent()) {
                logger.warn("Ride with ID: {} not found", id);
                throw new NotFoundException("Ride not found for ID : " + id);
            }
            Ride ride = rideOptional.get();
            ride.setStatus(statusDto.getStatus());
            Ride updatedRide = rideRepository.save(ride);
            RideResponseDto rideResponseDto = RideResponseDto.builder()
                    .dropPoint(updatedRide.getDropPoint())
                    .fare(updatedRide.getFare())
                    .distance(updatedRide.getDistance())
                    .driverName(updatedRide.getDriver().getUser().getName())
                    .driverContactNumber(Long.valueOf(updatedRide.getDriver().getUser().getMobileNumber()))
                    .vehicleNumber(updatedRide.getDriver().getVehicle().getLicensePlate())
                    .dropTime(updatedRide.getEndTime())
                    .build();
            logger.info("Successfully updated ride status for ride with ID: {}", id);
            return rideResponseDto;
        } catch (Exception e) {
            logger.error("Failed to update ride status for ride with ID: {}", id, e);
            throw new DatabaseException("Failed to update ride. Ride ID : " + id, e);
        }
    }


    @Override
    public Ride getRideByRideRequest(String id) {
        try {
            return rideRepository.findRideByRideRequestId(id);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while get ride by ride request id:" + id, e);
        }
    }

    @Override
    public void updateRideStatus(String driverId) {
        logger.debug("Updating status of the ride associated with driver ID: {}", driverId);
        try {
            Optional<Ride> rideOptional = rideRepository.findByDriverId(driverId);
            if (rideOptional.isEmpty()) {
                logger.warn("Ride associated with driver ID: {} not found", driverId);
                throw new NotFoundException("Ride not found for driver ID: " + driverId);
            }
            Ride ride = rideOptional.get();
            ride.setStatus(RIDE_COMPLETED);
            rideRepository.save(ride);
            emailSenderService.sendInvoiceMailtoCustomer(EmailInvoiceDto.builder()
                    .email(ride.getRideRequest().getCustomer().getUser().getEmail())
                    .pickupPoint(ride.getRideRequest().getPickupPoint())
                    .dropPoint(ride.getDropPoint())
                    .fare(ride.getFare()).build());
            logger.info("Successfully updated ride status for the ride associated with " +
                    "driver ID: {}", driverId);
        } catch (Exception e) {
            logger.error("Failed to update ride status for the ride associated with " +
                    "driver ID: {}", driverId, e);
            throw new DatabaseException("Failed to update ride status. Driver ID: " + driverId, e);
        }
    }

    @Override
    public PaymentModeDto paymentMode(String driverId, PaymentModeDto paymentModeDto) {
        logger.debug("Updating the payment mode for the ride associated with driver ID: {}", driverId);
        try {
            Optional<Ride> rideOptional = rideRepository.findByDriverId(driverId);
            if (rideOptional.isEmpty()) {
                logger.warn("Ride associated with driver ID: {} not found.", driverId);
                throw new NotFoundException("Ride not found for driver ID : " + driverId);
            }
            Ride ride = rideOptional.get();
            ride.setPaymentMode(paymentModeDto.getPaymentMode());
            rideRepository.save(ride);
            logger.info("Successfully updated the payment mode for the ride associated with " +
                    "driver ID: {}", driverId);
            return PaymentModeDto.builder()
                    .paymentMode(ride.getPaymentMode())
                    .build();
        } catch (Exception e) {
            logger.error("Failed to update the payment mode for the ride associated with " +
                    "driver ID: {}", driverId, e);
            throw new DatabaseException("Failed to update the payment mode. Driver ID : " + driverId, e);
        }
    }
}