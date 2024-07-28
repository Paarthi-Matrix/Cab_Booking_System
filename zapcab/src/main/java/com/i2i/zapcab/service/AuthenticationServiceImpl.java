package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.exception.DatabaseException;
import java.util.List;

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
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_REMARKS;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_STATUS_OF_DRIVER;

/**
 * <p>
 *     The `AuthenticationServiceImpl` responsible for registering and authenticating the users.
 *     The customer can directly register themselves to the database,
 *     but the driver cannot. They can be added only to the pending request table initially.
 *     After proper background and personal information verification admin will approve his request to
 *     act as driver. Then after the driver is added to the driver table.
 *     The users are categorized as follows
 * </p>
 * <ol>
 *     <li>Driver</li>
 *     <li>Customer</li>
 * </ol>
 * <p>For user and their role see {@link Role}</p>
 */
@Service
public class AuthenticationServiceImpl implements  AuthenticationService {

    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

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

    /**
     * <p>
     *     The `CustomerRegister` is responsible for registering the customer with ROLE_CUSTOMER.
     *     The customer tire is initially set to Bronze. Their tire is updated according to
     *     their history and frequency of rides. The INITIAL_CUSTOMER_TIRE is set in constants.
     * </p>
     * <p>
     *     Also Refer {@link com.i2i.zapcab.common.ZapCabConstant}
     * </p>
     *
     * @param registerCustomerDto {@link RegisterDriverRequestDto}
     *
     * @throws DatabaseException
     *         Arises while saving/updating the entity to the database.
     * @return AuthenticationResponseDto
     *         Contains JWT token upon successfull registration.
     */
    @Override
    @Transactional
    public AuthenticationResponseDto customerRegister(RegisterCustomerDto registerCustomerDto) {
        List<Role> roles = roleService.getByRoleType(registerCustomerDto.getRole());
        User user = User.builder()
                .name(registerCustomerDto.getName())
                .dateOfBirth(registerCustomerDto.getDateOfBirth())
                .email(registerCustomerDto.getEmail())
                .gender(registerCustomerDto.getGender())
                .mobileNumber(registerCustomerDto.getMobileNumber())
                .password(passwordEncoder.encode(registerCustomerDto.getPassword()))
                .role(roles)
                .build();
        Customer customer = Customer.builder()
                .tier(INITIAL_CUSTOMER_TIRE)
                .user(user)
                .build();
        userService.saveUsers(user);
        customerService.saveCustomer(customer);
        emailSenderService.sendRegistrationMailtoCustomer(registerCustomerDto);
        logger.info("Customer " + registerCustomerDto.getName() + " registered successfully!");
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken).build();
    }

    /**
     * <p>
     *      This method is used for authentication of both driver and customer.
     *      The user can use mobile number and password to sign in.
     *      Upon successful authentication a JWT token is returned.
     * </p>
     *
     * @param authenticationRequestDto {@link AuthenticationRequestDto}
     * @return AuthenticationResponseDto {@link AuthenticationResponseDto}
     */
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
            throw new AuthenticationException("Invalid credentials credentials",e);
        }
        logger.debug("Fetched user: " + (user != null ? user.getName() : "null"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwtToken).build();
    }

    /**
     * <p>
     *    This method is responsible for registering driver to the database.
     *    The drive cannot register as driver by themselves, due to some security constrains.
     *    Due to which
     * </p>
     * @param RegisterDriverRequestDto
     * @return
     */
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