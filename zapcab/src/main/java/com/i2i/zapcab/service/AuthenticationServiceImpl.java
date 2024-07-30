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
import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.CustomerRepository;

import static com.i2i.zapcab.common.ZapCabConstant.*;

/**
 * <p>
 * The `AuthenticationServiceImpl` responsible for registering and authenticating the users.
 * The customer can directly register themselves to the database,
 * but the driver cannot. They can be added only to the pending request table initially.
 * After proper background and personal information verification admin will approve his request to
 * act as driver. Then after the driver is added to the driver table.
 * The users are categorized as follows
 * </p>
 * <ol>
 *     <li>Driver</li>
 *     <li>Customer</li>
 * </ol>
 * <p>For user and their role see {@link Role}</p>
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PendingRequestService pendingRequestService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailSenderServiceImpl emailSenderService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthenticationResponseDto customerRegister(RegisterCustomerDto registerRequestDto) {
        List<Role> roles = roleService.getByRoleType(registerRequestDto.getRole());
        User user = User.builder()
                .name(registerRequestDto.getName())
                .dateOfBirth(registerRequestDto.getDateOfBirth())
                .email(registerRequestDto.getEmail())
                .gender(registerRequestDto.getGender())
                .mobileNumber(registerRequestDto.getMobileNumber())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(roles)
                .build();
        Customer customer = Customer.builder()
                .tier(INITIAL_CUSTOMER_TIRE)
                .user(user)
                .build();
        customerService.saveCustomer(customer);
        emailSenderService.sendRegistrationMailtoCustomer(registerRequestDto);
        logger.info("Customer {} registered successfully!", registerRequestDto.getName());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        User user = userService.getUserByMobileNumber(authenticationRequestDto.getMobileNumber());
        if (ObjectUtils.isEmpty(user)) {
            logger.info("No user with given mobile number is found in the database.");
            throw new NotFoundException("No user with phone number " +
                    authenticationRequestDto.getMobileNumber() + " found in database!");
        }
        try {
            logger.debug("Authenticating phone number: " +
                    authenticationRequestDto.getMobileNumber() + " with password.");
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
        logger.debug("Fetched user: {}", user != null ? user.getName() : "null");
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
        pendingRequestService.savePendingRequest(pendingRequest);
        return DriverRegisterResponseDto.builder()
                .status("Driver added to the pending request successfully!")
                .build();
    }
}