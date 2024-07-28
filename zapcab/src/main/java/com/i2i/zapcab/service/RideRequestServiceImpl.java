package com.i2i.zapcab.service;

import java.util.List;

import com.i2i.zapcab.common.FareCalculator;
import com.i2i.zapcab.dto.RideRequestResponseDto;
import com.i2i.zapcab.dto.UpdateRideResponseDto;
import com.i2i.zapcab.dto.UpdateRideDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.RideRequestDto;
import com.i2i.zapcab.mapper.RideRequestMapper;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.RideRequest;
import com.i2i.zapcab.repository.RideRequestRepository;
import org.springframework.util.ObjectUtils;

import static com.i2i.zapcab.common.ZapCabConstant.REQUEST_STATUS;
import java.time.LocalTime;
import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.ASSIGNED;

/**
 * Implements {@link RideRequestService}
 * Manages the business logic to perform operation like updating the status, assigning to the driver
 */
@Service
public class RideRequestServiceImpl implements RideRequestService {
    private static final Logger logger  = LogManager.getLogger(RideRequestServiceImpl.class);
    @Autowired
    RideRequestRepository rideRequestRepository;
    private final FareCalculator fareCalculator = new FareCalculator();

    private RideRequestMapper rideRequestMapper = new RideRequestMapper();

    @Override
    public List<RideRequest> getAll() {
        return rideRequestRepository.findAll();
    }

    @Override
    public RideRequest getRideByCustomerName(DriverSelectedRideDto selectedRideDto) {
        return rideRequestRepository.findByCustomerNameAndRideID(selectedRideDto.getCustomerName(), selectedRideDto.getRideId());
    }

    @Override
    public void updateRequest(RideRequest rideRequest) {
        rideRequestRepository.save(rideRequest);
    }

    @Override
    public boolean saveRideRequest(Customer customer, RideRequestDto rideRequestDto) {
        try {
            RideRequest rideRequest = rideRequestMapper.requestDtoToEntity(rideRequestDto);
            rideRequest.setStatus(REQUEST_STATUS);
            rideRequest.setRideTime(LocalTime.now().getHour());
            rideRequest.setCustomer(customer);
            return !ObjectUtils.isEmpty(rideRequestRepository.save(rideRequest));
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while saving ride request", e);
        }
    }

    @Override
    public void updateRideRequestStatus(String id) {
        try {
            RideRequest rideRequest = rideRequestRepository.findById(id).get();
            rideRequest.setStatus(ASSIGNED);
            rideRequestRepository.save(rideRequest);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while updating ride request status", e);
        }
    }

    @Override
    public RideRequest checkStatusAssignedOrNot(String id) {
        try {
            Optional<RideRequest> rideRequest = rideRequestRepository.findByCustomerId(id);
            return rideRequest.orElse(null);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while checking ride request status is assigned or not", e);
        }
    }

    /**
     * <p>
     *     Updates the details of a ride request based on the provided updateRideDto.
     * </p>
     * @param customerId
     *        The ID of the ride request to be updated.
     * @param updateRideDto
     *        The DTO containing the new details for the ride request.
     * @return UpdateResponseDto
     *         The response object containing the updated ride request details.
     * @throws NotFoundException
     *         If the ride request with the specified ID is not found.
     * @throws DatabaseException
     *         If an error occurs while updating the ride request details.
     */

    public UpdateRideResponseDto updateRideDetails(String customerId, UpdateRideDto updateRideDto) {
        try {
            Optional<RideRequest> rideRequestOptional = rideRequestRepository.findByCustomerId(customerId);
            if (rideRequestOptional.isEmpty()) {
                logger.warn("Ride with customer ID: {} not found.", customerId);
                throw new NotFoundException("Ride not found for customer ID: " + customerId);
            }
            RideRequest rideRequest = rideRequestOptional.get();
            rideRequest.setPickupPoint(updateRideDto.getPickupPoint());
            rideRequest.setDropPoint(updateRideDto.getDropPoint());
            rideRequest.setVehicleCategory(updateRideDto.getVehicleCategory());
            RideRequestResponseDto fareResponse = fareCalculator.calculateFare(updateRideDto.getPickupPoint(),
                     updateRideDto.getDropPoint(), updateRideDto.getVehicleCategory());
            if (null != fareResponse) {
                rideRequest.setFare(fareResponse.getFare());
                rideRequest.setDistance(fareResponse.getDistance());
            }
            rideRequestRepository.save(rideRequest);
            UpdateRideResponseDto updateRideResponseDto = UpdateRideResponseDto.builder()
                    .pickupPoint(rideRequest.getPickupPoint())
                    .dropPoint(rideRequest.getDropPoint())
                    .vehicleCategory(rideRequest.getVehicleCategory())
                    .fare(rideRequest.getFare())
                    .distance(rideRequest.getDistance())
                    .build();
            return updateRideResponseDto;
        } catch (Exception e) {
            logger.error("Failed to update ride details for customer with ID: {}", customerId);
            throw new DatabaseException("Failed to update ride details. Customer ID: " + customerId, e);
        }
    }
}
