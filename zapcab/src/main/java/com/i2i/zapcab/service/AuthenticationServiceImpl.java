package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.mapper.PendingRequestMapper;
import java.util.List;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.mapper.AuthenticationMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.Customer;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.CustomerRepository;


import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_CUSTOMER_TIRE;

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
public class AuthenticationServiceImpl implements  AuthenticationService {

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
    private final AuthenticationMapper authenticationMapper = new AuthenticationMapper();

    @Override
    @Transactional
    public AuthenticationResponseDto customerRegister(RegisterCustomerDto registerCustomerDto) {
        if (!ObjectUtils.isEmpty(userService.getUserByMobileNumber(registerCustomerDto.getMobileNumber()))){
            logger.warn("User already exist with mobile number {} in database", registerCustomerDto.getMobileNumber());
            return null;
        }
        logger.debug("Initiated customer register");
        List<RoleEnum> roleEnums = List.of(RoleEnum.CUSTOMER);
        List<Role> roles = roleService.getByRoleType(roleEnums);
        registerCustomerDto.setPassword(passwordEncoder.encode(registerCustomerDto.getPassword()));
        User user = authenticationMapper.toUser(registerCustomerDto, roles);
        Customer customer = Customer.builder()
                .tier(INITIAL_CUSTOMER_TIRE)
                .user(user)
                .build();
        customerService.saveCustomer(customer);
        emailSenderService.sendRegistrationMailtoCustomer(registerCustomerDto);
        logger.info("Customer {} registered successfully!", registerCustomerDto.getName());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        User user = userService.getUserByMobileNumber(authenticationRequestDto.getMobileNumber());
        if (ObjectUtils.isEmpty(user)) {
            logger.error("No user with given mobile number is found in the database.");
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
            logger.error("Authentication failed: " + e.getMessage());
            throw new AuthenticationException("Invalid credentials credentials", e);
        }
        logger.info("Fetched user: " + (user != null ? user.getName() : "null"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwtToken).build();
    }

    @Override
    @Transactional
    public DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto) {
        PendingRequest pendingRequest = authenticationMapper.toPendingRequest(registerDriverRequestDto);
        if (!ObjectUtils.isEmpty(pendingRequestService.findRequestByMobileNumber(pendingRequest.getMobileNumber()))) {
            logger.warn("User already exist with mobile number {} in database", registerDriverRequestDto.getMobileNumber());
            return null;
        }
        pendingRequestService.savePendingRequest(pendingRequest);
        logger.info("Driver saved to pending request successfully.");
        return DriverRegisterResponseDto.builder()
                .status("Driver added to the pending request successfully!")
                .build();
    }
}