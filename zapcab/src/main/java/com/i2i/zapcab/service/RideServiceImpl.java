package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.PaymentModeDto;
import com.i2i.zapcab.dto.RideInvoiceDto;
import com.i2i.zapcab.dto.RideRatingDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.dto.RideResponseDto;
import com.i2i.zapcab.dto.StatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.mapper.RideMapper;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.model.Ride;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RideServiceImpl implements RideService{
    private static final Logger logger  = LogManager.getLogger(RideServiceImpl.class);
    private RideMapper rideMapper = new RideMapper();
    @Autowired
    private RideRepository rideRepository;


    @Override
    public void saveRide(RideRequest rideRequest, Driver driver) {
        try {
            logger.info("Saving the request {} to the ride table....",rideRequest.getPickupPoint());
            Ride ride = rideMapper.rideRequestToRide(rideRequest, driver);
            rideRepository.save(ride);
            logger.info("Successfully ride has been saved {}",rideRequest.getPickupPoint());
        } catch(Exception e) {
            logger.error("Unable to save the ride requested by the customer : {}",
                    rideRequest.getCustomer().getUser().getName());
            throw new UnexpectedException("Unable to save the ride for the request : "+ rideRequest.getId(), e);
        }
    }

    @Override
    public int updateRideRating(int id, RideRatingDto ratings) {
        try {
            Ride ride = rideRepository.findById(id).get();
            ride.setRideRating(ratings.getRatings());
            rideRepository.save(ride);
            return ride.getDriver().getId();
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while updating ride rating with its id: " + id, e);
        }
    }

    @Override
    public Ride getRideByRideRequest(int id) {
        try {
            return rideRepository.findRideByRideRequestId(id);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while get ride by ride request id:" + id, e);
        }
    }

    /**
     * <p>
     *     Updates the status of an existing ride.
     * </p>
     * @param id
     *        The ID of the ride whose status needs to be updated.
     * @param statusDto {@link StatusDto}
     *        The StatusDto object containing the new status of the ride.
     * @return RideResponseDto
     *         The response object containing the updated ride details.
     * @throws UnexpectedException
     *         If error occurs while updating the ride status.
     */
    public RideResponseDto updateRideStatus(int id, StatusDto statusDto) {
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
            throw new UnexpectedException("Failed to update ride. Ride ID : " + id, e);
        }
    }


    /**
     * <p>
     *     Generates an invoice for a completed ride.
     * </p>
     * @param rideId
     *        The ID of the ride for which the invoice is to be generated.
     * @return RideInvoiceDto
     *         The response object containing the ride invoice details.
     * @throws UnexpectedException
     *         If error occurs while retrieving the ride invoice.
     */
    public RideInvoiceDto generateRideInvoice(int rideId) {
        logger.debug("Generating invoice for completed ride with ID: {}", rideId);

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> {
                    logger.warn("Ride with ID: {} not found", rideId);
                    return new NotFoundException("Ride not found for ID: " + rideId);
                });

        RideInvoiceDto rideInvoiceDto = RideInvoiceDto.builder()
                .rideId(ride.getId())
                .driverName(ride.getDriver().getUser().getName())
                .driverContactNumber(ride.getDriver().getUser().getMobileNumber())
                .vehicleNumber(ride.getDriver().getVehicle().getLicensePlate())
                .pickupPoint(ride.getRideRequest().getPickupPoint())
                .dropPoint(ride.getDropPoint())
                .fare(ride.getFare())
                .build();

        logger.info("Successfully generated invoice for ride with ID: {}", rideId);
        return rideInvoiceDto;
    }


    /**
     * <p>
     *     Tracks the status of an existing ride.
     * </p>
     * @param id
     *        The ID of the ride whose status needs to be tracked.
     * @return RideResponseDto
     *         The response object containing the current ride status.
     * @throws UnexpectedException
     *         If error occurs while retrieving the ride details.
     */
    public RideResponseDto trackRideStatus(int id) {
        logger.debug("Tracking status for ride with ID: {}", id);
        try {
            Optional<Ride> rideOptional = rideRepository.findById(id);
            if (!rideOptional.isPresent()) {
                logger.warn("Ride with ID: {} not found.", id);
                throw new NotFoundException("Ride not found for ID : " + id);
            }
            Ride ride = rideOptional.get();
            RideResponseDto rideResponseDto = RideResponseDto.builder()
                    .dropPoint(ride.getDropPoint())
                    .fare(ride.getFare())
                    .distance(ride.getDistance())
                    .driverName(ride.getDriver().getUser().getName())
                    .driverContactNumber(Long.valueOf(ride.getDriver().getUser().getMobileNumber()))
                    .vehicleNumber(ride.getDriver().getVehicle().getLicensePlate())
                    .dropTime(ride.getEndTime())
                    .build();
            logger.info("Successfully retrieved status for ride with ID: {}", id);
            return rideResponseDto;
        } catch (Exception e) {
            logger.error("Failed to track ride status for ride with ID: {}", id, e);
            throw new UnexpectedException("Failed to track ride status. Ride ID : " + id, e);
        }
    }

    /**
     * <p>
     *     Tracks the status of an existing ride.
     * </p>
     * @param id
     *        The ID of the ride whose payment mode to be updated.
     * @return PaymentModeDto
     *         The response object containing the current payment mode.
     * @throws UnexpectedException
     *         If error occurs while updating the payment mode details.
     */
    public PaymentModeDto paymentMode(int id, PaymentModeDto paymentModeDto) {
        logger.debug("Updating the payment mode for ride with ID: {}", id);
        try {
            Optional<Ride> rideOptional = rideRepository.findById(id);
            if (!rideOptional.isPresent()) {
                logger.warn("Ride with ID: {} not found.", id);
                throw new NotFoundException("Ride not found for ID : " + id);
            }
            Ride ride = rideOptional.get();
            ride.setPaymentMode(ride.getPaymentMode());
            Ride updatedMode = rideRepository.save(ride);
            logger.info("Successfully updated the payment mode for ride with ID: {}", id);
            return paymentModeDto;
        } catch (Exception e) {
            logger.error("Failed to update the payment mode for ride with ID: {}", id, e);
            throw new UnexpectedException("Failed to update the payment mode. Ride ID : " + id, e);
        }
    }
}