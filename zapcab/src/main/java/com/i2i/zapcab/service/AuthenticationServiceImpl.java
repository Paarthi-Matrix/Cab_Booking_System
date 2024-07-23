package com.i2i.zapcab.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.i2i.zapcab.config.JwtService;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerRequestDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.CustomerRepository;
import com.i2i.zapcab.repository.PendingRequestRepository;
import com.i2i.zapcab.repository.RoleRepository;
import com.i2i.zapcab.repository.UserRepository;

import static com.i2i.zapcab.constant.ZapCabConstant.*;

@Service
public class AuthenticationServiceImpl implements  AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository repository;
    @Autowired
    private PendingRequestRepository pendingRequestRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Override
    @Transactional
    public AuthenticationResponseDto customerRegister(RegisterCustomerRequestDto registerRequestDto) {
        List<Role> roles = repository.findByRoleType(registerRequestDto.getRole());
        User user = User.builder()
                .name(registerRequestDto.getName())
                .dateOfBirth(registerRequestDto.getDateOfBirth())
                .email(registerRequestDto.getEmail())
                .gender(registerRequestDto.getGender())
                .mobileNumber(registerRequestDto.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(roles)
                .build();
        Customer customer = Customer.builder()
                .tier(INITIAL_CUSTOMER_TIRE)
                .user(user)
                .build();
        userRepository.save(user);
        customerRepository.save(customer);
        logger.info("Customer registered successfully!");
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthenticationResponseDto authenticate (AuthenticationRequestDto authenticationRequestDto) {
        User user = userRepository.findByMobileNumber(authenticationRequestDto.getPhoneNumber());
        if (ObjectUtils.isEmpty(user)) {
            logger.info("No user with given mobile number is found in the database.");
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
            throw new AuthenticationException("Invalid credentials credentials",e);
        }
        logger.debug("Fetched user: " + (user != null ? user.getName() : "null"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwtToken).build();
    }

    @Override
    @Transactional
    public DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto) {
        PendingRequest pendingRequest = PendingRequest.builder()
                .name(registerDriverRequestDto.getName())
                .email(registerDriverRequestDto.getEmail())
                .region(registerDriverRequestDto.getRegion())
                .licenseNo(registerDriverRequestDto.getLicenseNumber())
                .rcBookNo(registerDriverRequestDto.getRcBookNo())
                .status(INITIAL_STATUS_OF_DRIVER)
                .city(registerDriverRequestDto.getCity())
                .dob(registerDriverRequestDto.getDateOfBirth())
                .gender(registerDriverRequestDto.getGender())
                .mobileNumber(registerDriverRequestDto.getMobileNumber())
                .category(registerDriverRequestDto.getCategory())
                .model(registerDriverRequestDto.getModel())
                .type(registerDriverRequestDto.getType())
                .remarks(INITIAL_REMARKS)
                .licensePlate(registerDriverRequestDto.getLicensePlate())
                .build();
        pendingRequestRepository.save(pendingRequest);
        return DriverRegisterResponseDto.builder()
                .status("Driver added to the pending request successfully!")
                .build();
    }
}