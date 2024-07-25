package com.i2i.zapcab.service;

import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.helper.DriverStatusEnum;
import com.i2i.zapcab.helper.PinGeneration;
import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.REQUEST_STATUS;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);
    @Autowired
    PendingRequestService pendingRequestService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    DriverService driverService;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @Override
    public Page<FetchAllPendingRequestsDto> pendingRequestProcessing(int page, int size) {
        return pendingRequestService.getAllPendingRequests(page, size).map(pendingRequest -> {
            FetchAllPendingRequestsDto fetchAllPendingRequestsDto = FetchAllPendingRequestsDto.builder()
                    .name(pendingRequest.getName())
                    .dob(pendingRequest.getDob()).email(pendingRequest.getEmail())
                    .city(pendingRequest.getCity()).mobileNumber(pendingRequest.getMobileNumber())
                    .category(pendingRequest.getCategory()).model(pendingRequest.getModel())
                    .licenseNo(pendingRequest.getLicenseNo()).rcBookNo(pendingRequest.getRcBookNo())
                    .licensePlate(pendingRequest.getLicensePlate()).status(pendingRequest.getStatus())
                    .remarks(pendingRequest.getRemarks()).build();
            return fetchAllPendingRequestsDto;
        });

    }

    @Override
    public AuthenticationResponseDto modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto) {
        logger.info("Starting to update pending request for phone number: {}",
                updatePendingRequestDto.getPhoneNumber());
        try {
            AuthenticationResponseDto authenticationResponse = new AuthenticationResponseDto();
            Optional<PendingRequest> pendingRequest = pendingRequestService.findRequestByMobileNumber(updatePendingRequestDto.getPhoneNumber());
            if (pendingRequest.isPresent()) {
                PendingRequest request = pendingRequest.get();
                if (REQUEST_STATUS.equalsIgnoreCase(request.getStatus())) {
                    logger.info("Updating pending request status for phone number: {}",
                            updatePendingRequestDto.getPhoneNumber());
                    request.setStatus(updatePendingRequestDto.getStatus());
                    request.setRemarks(updatePendingRequestDto.getRemarks());
                    authenticationResponse = driverRegister(request);
                    pendingRequestService.savePendingRequest(request);
                    logger.info("Pending request successfully updated for phone number: {}",
                            updatePendingRequestDto.getPhoneNumber());
                } else {
                    logger.warn("Pending request status not matching for phone number: {}",
                            updatePendingRequestDto.getPhoneNumber());
                }
            } else {
                logger.warn("Pending request not found for phone number: {}",
                        updatePendingRequestDto.getPhoneNumber());
            }
            return authenticationResponse;
        } catch (Exception e) {
            logger.error("Error updating pending request for phone number: {}",
                    updatePendingRequestDto.getPhoneNumber(), e);
            throw new AuthenticationException("Cant able to update the pending requests for the driver : ");
        }
    }

    /**
     * Used to register the driver once the user is approved.
     *
     * @param pendingRequest {@link PendingRequest}
     * @return AuthenticationResponse which holds a unique token for that particular user.
     */
    private AuthenticationResponseDto driverRegister(PendingRequest pendingRequest) {
        try {
            logger.debug("Starting driver registration process for email: {}", pendingRequest.getEmail());
            List<RoleEnum> roleEnums = List.of(RoleEnum.values());
            List<Role> roles = roleService.getByRoleType(roleEnums);
            User user = User.builder().name(pendingRequest.getName()).dateOfBirth(pendingRequest.getDob())
                    .email(pendingRequest.getEmail()).gender(pendingRequest.getGender())
                    .mobileNumber(pendingRequest.getMobileNumber())
                    .password(passwordEncoder.encode(PinGeneration.driverPasswordGeneration())).role(roles)
                    .build();
            Vehicle vehicle = Vehicle.builder().category(pendingRequest.getCategory()).type(pendingRequest.getType())
                    .model(pendingRequest.getModel()).licensePlate(pendingRequest.getLicensePlate())
                    .maxSeats(4).status("available").build();
            Driver driver = Driver.builder().region(pendingRequest.getRegion()).noOfCancellation(2)
                    .licenseNo(pendingRequest.getLicenseNo()).rcBookNo(pendingRequest.getRcBookNo()).user(user)
                    .status(String.valueOf(DriverStatusEnum.OnDuty)).vehicle(vehicle).build();
            driverService.saveDriver(driver);
            logger.info("Driver registration successful for email: {}", pendingRequest.getEmail());
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponseDto.builder()
                    .token(jwtToken).build();
        } catch (Exception e) {
            logger.error("Driver registration failed for email: {}", pendingRequest.getEmail(), e);
            throw new AuthenticationException("Driver registration failed for : " + pendingRequest.getName() +
                    "\t mobile number : " + pendingRequest.getMobileNumber(), e);
        }
    }
}
