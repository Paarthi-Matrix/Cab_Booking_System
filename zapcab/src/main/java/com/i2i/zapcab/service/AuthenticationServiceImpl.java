package com.i2i.zapcab.service;

import java.util.List;

import com.i2i.zapcab.dto.RegisterUserRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.RegisterDriverDto;
import com.i2i.zapcab.model.*;
import com.i2i.zapcab.repository.PendingRequestsRepository;
import com.i2i.zapcab.repository.RoleRepository;
import com.i2i.zapcab.repository.UserRepository;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository repository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PendingRequestsRepository pendingRequestsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse customerRegister(RegisterUserRequestDto registerRequestDto) {
        try {
            logger.debug("Starting customer registration for email: {}", registerRequestDto.getEmail());
            List<Role> roles = repository.findByRoleType(registerRequestDto.getRole());
            User user = User.builder().name(registerRequestDto.getName()).dateOfBirth(registerRequestDto.getDateOfBirth())
                    .email(registerRequestDto.getEmail()).gender(registerRequestDto.getGender())
                    .mobileNumber(registerRequestDto.getMobileNumber()).password(passwordEncoder.encode(registerRequestDto.getPassword()))
                    .role(roles).build();
            Customer customer = Customer.builder().tier("Bronze").user(user).build();
            userRepository.save(user);
            customerRepository.save(customer);
            logger.debug("Customer registration successful for email: {}", registerRequestDto.getEmail());
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (Exception e) {
            logger.error("Customer registration failed for email: {}", registerRequestDto.getEmail(), e);
            throw new AuthenticationException("Cannot register customer: " + registerRequestDto.getEmail(), e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDto authenticationRequestDto) {
        User user = userRepository.findByMobileNumber(authenticationRequestDto.getPhoneNumber());

        if (ObjectUtils.isEmpty(user)) {
            throw new NotFoundException("No user with phone number " +
                    authenticationRequestDto.getPhoneNumber() +
                    " found in database!");
        }
        try {
            logger.debug("Authenticating phone number: " +
                    authenticationRequestDto.getPhoneNumber() +
                    " with password.");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getId(),
                            authenticationRequestDto.getPassword()
                    )
            );
        } catch (Exception e) {
            logger.debug("Authentication failed: " + e.getMessage());
            throw new AuthenticationException("Invalid credentials credentials", e);
        }
        logger.debug("Fetched user: " + (user != null ? user.getName() : "null"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse adminRegister(RegisterUserRequestDto registerRequestDto) {
        List<Role> roles = repository.findByRoleType(registerRequestDto.getRole());
        User user = User.builder().name(registerRequestDto.getName())
                .dateOfBirth(registerRequestDto.getDateOfBirth()).email(registerRequestDto.getEmail())
                .gender(registerRequestDto.getGender()).mobileNumber(registerRequestDto.getMobileNumber())
                .password(passwordEncoder.encode(registerRequestDto.getPassword())).role(roles)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse driverRequest(RegisterDriverDto registerDriverDto) {
        PendingRequest pendingRequest = PendingRequest.builder()
                .name(registerDriverDto.getName())
                .email(registerDriverDto.getEmail())
                .region(registerDriverDto.getRegion())
                .licenseNo(registerDriverDto.getLicenseNumber())
                .rcBookNo(registerDriverDto.getRcBookNo())
                .status("pending")
                .city(registerDriverDto.getCity())
                .dob(registerDriverDto.getDateOfBirth())
                .gender(registerDriverDto.getGender())
                .mobileNumber(registerDriverDto.getMobileNumber())
                .category(registerDriverDto.getCategory())
                .model(registerDriverDto.getModel())
                .type(registerDriverDto.getType())
                .licensePlate(registerDriverDto.getLicensePlate())
                .build();
        pendingRequestsRepository.save(pendingRequest);
        return AuthenticationResponse.builder().token("Applied successfully").build();
    }
}