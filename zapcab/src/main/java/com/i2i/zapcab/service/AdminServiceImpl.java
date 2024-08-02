package com.i2i.zapcab.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.EmailRequestDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.helper.PinGeneration;
import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.mapper.PendingRequestMapper;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.model.VehicleLocation;

import static com.i2i.zapcab.common.ZapCabConstant.*;

/**
 * <p>
 * Implements {@link AdminService}
 * This class provides business logic for the methods that are handled by admin.
 * The related operation includes :
 *    <ul>
 *        <li>Listing all the requests</li>
 *        <li>Updating the user's request status</li>
 *    </ul>
 * </p>
 */
@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);
    PendingRequestMapper pendingRequestMapper = new PendingRequestMapper();

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PendingRequestService pendingRequestService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private VehicleLocationService vehicleLocationService;
    @Autowired
    EmailSenderService emailSenderService;

    @Override
    public Page<FetchAllPendingRequestsDto> getAllPendingRequest(int page, int size) {
        try {
            logger.debug("Retrieving the pending request");
            return pendingRequestService.getAllPendingRequests(page, size).map(pendingRequest ->
                    pendingRequestMapper.entityToRequestDto(pendingRequest));
        } catch (Exception e) {
            logger.error("Error occurred while fetching the list of pending requests", e);
            throw new DatabaseException("Unable to retrieve the pending requests list", e);
        }
    }

    @Override
    public AuthenticationResponseDto updatePendingRequest(UpdatePendingRequestDto updatePendingRequestDto) {
        logger.debug("Starting to update pending request for phone number: {}",
                updatePendingRequestDto.getMobileNumber());
        try {
            AuthenticationResponseDto authenticationResponse = new AuthenticationResponseDto();
            Optional<PendingRequest> pendingRequest = pendingRequestService.findRequestByMobileNumber(updatePendingRequestDto.getMobileNumber());
            if (pendingRequest.isPresent()) {
                PendingRequest request = pendingRequest.get();
                logger.debug("Updating pending request status for phone number: {}",
                        updatePendingRequestDto.getMobileNumber());
                request.setStatus(updatePendingRequestDto.getStatus());
                request.setRemarks(updatePendingRequestDto.getRemarks());
                pendingRequestService.savePendingRequest(request);
                if (request.getStatus().equalsIgnoreCase(REJECTED)) {
                    emailSenderService.sendRejectionEmailToDriver(
                            EmailRequestDto.builder()
                                    .toEmail(pendingRequest.get().getEmail())
                                    .UserName(pendingRequest.get().getName())
                                    .remarks(updatePendingRequestDto.getRemarks())
                                    .build()
                    );
                    logger.info("The application has been rejected because {}", updatePendingRequestDto.getRemarks());
                    return AuthenticationResponseDto.builder().token(null).build();
                }
                logger.info("Pending request successfully updated for phone number: {}",
                        updatePendingRequestDto.getMobileNumber());
                return driverRegister(request);
            } else {
                logger.warn("Pending request not found for phone number: {}",
                        updatePendingRequestDto.getMobileNumber());
            }
            return authenticationResponse;
        } catch (Exception e) {
            logger.error("Error while updating pending request for phone number: {}",
                    updatePendingRequestDto.getMobileNumber(), e);
            throw new DatabaseException("Can't update the pending requests for the driver : ");
        }
    }

    /**
     * <p>
     * Used to register the driver once the user is approved.
     * </p>
     *
     * @param pendingRequest {@link PendingRequest}
     * @return AuthenticationResponse which holds a unique token for that particular user.
     */
    private AuthenticationResponseDto driverRegister(PendingRequest pendingRequest) {
        try {
            logger.debug("Starting driver registration process for email: {}", pendingRequest.getEmail());
            List<RoleEnum> roleEnums = List.of(RoleEnum.DRIVER);
            List<Role> roles = roleService.getByRoleType(roleEnums);
            String password = PinGeneration.driverPasswordGeneration();
            int maxSeats = determineMaxSeatsByCategory(pendingRequest);
            if (maxSeats == -1) {
                return null;
            }
            User user = User.builder().name(pendingRequest.getName()).dateOfBirth(pendingRequest.getDob())
                    .email(pendingRequest.getEmail()).gender(pendingRequest.getGender())
                    .mobileNumber(pendingRequest.getMobileNumber())
                    .password(passwordEncoder.encode(password))
                    .role(roles)
                    .build();
            VehicleLocation vehicleLocation = VehicleLocation.builder().location(pendingRequest.getRegion())
                    .build();
            Vehicle vehicle = Vehicle.builder().category(pendingRequest.getCategory().toUpperCase())
                    .type(pendingRequest.getType())
                    .vehicleLocation(vehicleLocation).model(pendingRequest.getModel())
                    .licensePlate(pendingRequest.getLicensePlate())
                    .maxSeats(maxSeats).status(VEHICLE_STATUS_UNAVAILABLE).build();
            vehicleLocation.setVehicle(vehicle);
            Driver driver = Driver.builder().region(pendingRequest.getRegion()).noOfCancellation(2)
                    .wallet(INITIAL_WALLET_AMOUNT).licenseNo(pendingRequest.getLicenseNo())
                    .rcBookNo(pendingRequest.getRcBookNo()).user(user).status(INITIAL_DRIVER_STATUS)
                    .vehicle(vehicle).build();
            driverService.saveDriver(driver);
            emailSenderService.sendRegistrationEmailToDriver(
                    EmailRequestDto.builder()
                            .toEmail(pendingRequest.getEmail())
                            .UserName(pendingRequest.getName())
                            .mobilNumber(pendingRequest.getMobileNumber())
                            .password(password).build()
            );
            logger.info("Driver registration successful for email: {}", pendingRequest.getEmail());
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponseDto.builder()
                    .token(jwtToken).build();
        } catch (Exception e) {
            logger.error("Driver registration failed for email: {}", pendingRequest.getEmail(), e);
            throw new DatabaseException("Driver registration failed for : " + pendingRequest.getName() +
                    "\t mobile number : " + pendingRequest.getMobileNumber(), e);
        }
    }

    private int determineMaxSeatsByCategory(PendingRequest pendingRequest) {
        if (pendingRequest.getCategory().equalsIgnoreCase("AUTO")) {
            return AUTO_MAX_SEATS;
        } else if (pendingRequest.getCategory().equalsIgnoreCase("SEDAN")
                    || pendingRequest.getCategory().equalsIgnoreCase("XUV")) {
            return SEDAN_OR_XUV_MAX_SEATS;
        } else if (pendingRequest.getCategory().equalsIgnoreCase("BIKE")) {
            return BIKE_MAX_SEATS;
        } else {
            return -1;
        }
    }
}