package com.i2i.zapcab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.helper.PinGeneration;
import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.model.Vehicle;
import com.i2i.zapcab.repository.PendingRequestRepository;

import static com.i2i.zapcab.constant.ZapCabConstant.INITIAL_DRIVER_STATUS;
import static com.i2i.zapcab.constant.ZapCabConstant.INITIAL_VEHICLE_STATUS;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PendingRequestRepository pendingRequestRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PendingRequestService pendingRequestService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<PendingRequest> pendingRequestProcessing(int page, int size) {
        Page<PendingRequest> requests = pendingRequestService.getAllPendingRequests(page, size);

        return requests;
    }

    @Override
    @Transactional
    public AuthenticationResponseDto modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto) {
        Optional<PendingRequest> pendingRequest = pendingRequestRepository.findByMobileNumber(updatePendingRequestDto.getPhoneNumber());
        AuthenticationResponseDto authenticationResponse = null;
        if(pendingRequest.isPresent()){
            PendingRequest request = pendingRequest.get();
            request.setStatus(updatePendingRequestDto.getStatus());
            request.setRemarks(updatePendingRequestDto.getRemarks());
            authenticationResponse = driverRegister(request);
            pendingRequestService.savePendingRequest(request);
        }
        return authenticationResponse;
    }

    @Transactional
    private AuthenticationResponseDto driverRegister(PendingRequest request) {
        List<RoleEnum> roleEnums = List.of(RoleEnum.values());
        List<Role> roles = roleService.getByRoleType(roleEnums);
        User user = User.builder()
                .name(request.getName())
                .dateOfBirth(request.getDob())
                .email(request.getEmail())
                .gender(request.getGender())
                .mobileNumber(request.getMobileNumber())
                .role(roles)
                .password(passwordEncoder.encode(PinGeneration.driverPasswordGeneration()))
                .build();
        Vehicle vehicle = Vehicle.builder()
                .category(request.getCategory())
                .type(request.getType())
                .model(request.getModel())
                .licensePlate(request.getLicensePlate())
                .maxSeats(4)
                .status(INITIAL_VEHICLE_STATUS)
                .build();
        Driver driver = Driver.builder()
                .region(request.getRegion())
                .noOfCancellation(2)
                .licenseNo(request.getLicenseNo())
                .rcBookNo(request.getRcBookNo())
                .user(user)
                .status(INITIAL_DRIVER_STATUS)
                .vehicle(vehicle)
                .build();
        driverService.saveDriver(driver);
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwt).build();
    }
}
