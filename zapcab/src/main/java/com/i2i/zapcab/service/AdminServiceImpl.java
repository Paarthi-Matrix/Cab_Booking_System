package com.i2i.zapcab.service;

import java.util.List;
import java.util.Optional;
import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.helper.DriverStatusEnum;
import com.i2i.zapcab.helper.PinGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.repository.PendingRequestsRepository;


import static com.i2i.zapcab.constant.ZapCabConstant.REQUEST_STATUS;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);
    @Autowired
    PendingRequestsRepository pendingRequestsRepository;
    @Autowired
    DriverService driverService;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @Override
    public List<PendingRequest> getAllPendingRequests() {
        logger.debug("Entering into the retrieving the pending requests");
        try {
            List<PendingRequest> requests = pendingRequestsRepository.findAll();
            logger.info("List of requests has been retrieved");
            return requests;
        } catch (Exception e) {
            logger.error("Unable to retrieve the lists of request");
            throw new AuthenticationException("Error occurred while retrieving all the pending requests", e);
        }
    }
    @Override
    public AuthenticationResponse modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto) {
        logger.info("Starting to update pending request for phone number: {}",
                updatePendingRequestDto.getPhoneNumber());
        try {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            Optional<PendingRequest> pendingRequest = pendingRequestsRepository.findByMobileNumber(updatePendingRequestDto.getPhoneNumber());
            if (pendingRequest.isPresent()) {
                PendingRequest request = pendingRequest.get();
                if (REQUEST_STATUS.equalsIgnoreCase(request.getStatus())) {
                    logger.info("Updating pending request status for phone number: {}",
                            updatePendingRequestDto.getPhoneNumber());
                    request.setStatus(updatePendingRequestDto.getStatus());
                    request.setRemarks(updatePendingRequestDto.getRemarks());
                    authenticationResponse = driverRegister(request);
                    pendingRequestsRepository.save(request);
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
        } catch (Exception e ) {
            logger.error("Error updating pending request for phone number: {}",
                    updatePendingRequestDto.getPhoneNumber(), e);
            throw new AuthenticationException("Cant able to update the pending requests for the driver : "
                    +updatePendingRequestDto.getPhoneNumber(), e);
        }
    }

    private AuthenticationResponse driverRegister(PendingRequest request) {
        try {
            logger.debug("Starting driver registration process for email: {}", request.getEmail());
            List<RoleEnum> roleEnums = List.of(RoleEnum.values());
            List<Role> roles = roleService.getByRoleType(roleEnums);
            User user = User.builder().name(request.getName()).dateOfBirth(request.getDob()).email(request.getEmail())
                    .gender(request.getGender()).mobileNumber(request.getMobileNumber())
                    .password(passwordEncoder.encode(PinGeneration.driverPasswordGeneration())).role(roles)
                    .build();
            Vehicle vehicle = Vehicle.builder().category(request.getCategory()).type(request.getType()).model(request.getModel())
                    .licensePlate(request.getLicensePlate()).maxSeats(4).status("available").build();
            Driver driver = Driver.builder().region(request.getRegion()).noOfCancellation(2)
                    .licenseNo(request.getLicenseNo()).rcBookNo(request.getRcBookNo()).user(user)
                    .status(String.valueOf(DriverStatusEnum.OnDuty)).vehicle(vehicle).build();
            driverService.saveDriver(driver);
            logger.info("Driver registration successful for email: {}", request.getEmail());
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (Exception e) {
            logger.error("Driver registration failed for email: {}", request.getEmail(), e);
            throw new AuthenticationException("Driver registration failed for : "+request.getName()+
                    "\t mobile number : "+request.getMobileNumber(), e);
        }
    }
}
