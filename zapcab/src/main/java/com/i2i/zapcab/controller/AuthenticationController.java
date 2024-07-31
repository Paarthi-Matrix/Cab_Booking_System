package com.i2i.zapcab.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.service.AuthenticationService;
import com.i2i.zapcab.service.AuthenticationServiceImpl;

/**
 * <p>
 * The AuthenticationController class handles all authentication-related requests for the application.
 * It includes endpoints for registering customers and drivers, as well as authenticating users.
 * </p>
 * <p>
 * This controller is responsible for processing the registration and authentication requests.
 * It ensures that the input data is received correctly,
 * processes the data through the service layer, and returns appropriate responses to the client.
 * </p>
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    AuthenticationService authenticationService;

    /**
     * <p>
     * This method is responsible for registering customer (Customer Sign up).
     * </p>
     *
     * @param registerCustomerDto {@link RegisterCustomerDto}
     *                            The request body must contain all the fields of RegisterCustomerRequestDto.
     * @return ApiResponseDto<AuthenticationResponse> {@link ApiResponseDto}
     * Returns the JWT Token with 201 CREATED upon successful registration .
     * Else returns 500 INTERNAL_SERVER_ERROR.
     */
    @PostMapping("/customers/register")
    public ApiResponseDto<AuthenticationResponseDto> registerCustomer(
            @RequestBody RegisterCustomerDto registerCustomerDto) {
        AuthenticationResponseDto authenticationResponseDto = null;
        try {
            logger.info("Received customer registration request for: {}", registerCustomerDto.getEmail());
            authenticationResponseDto = authenticationService.customerRegister(registerCustomerDto);
            logger.info("Customer registration successful for: {}", registerCustomerDto.getEmail());
            return ApiResponseDto.statusCreated(authenticationResponseDto);
        } catch (DatabaseException e) {
            logger.error("Error occurred while registering customer: {}. Error: {}", registerCustomerDto.getEmail(),
                    e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(authenticationResponseDto, e);
        }
    }

    /**
     * <p>
     * This method is responsible for registering driver.
     * </p>
     *
     * @param registerDriverRequestDto {@link DriverRegisterResponseDto}
     *                                 The request body must contain all the fields of the RegisterDriverDto.
     * @return ApiResponseDto<DriverRegisterRequestResponseDto> {@link ApiResponseDto}
     * Returns the JWT Token with 201 CREATED upon successful registration .
     * Else returns 500 INTERNAL_SERVER_ERROR.
     */
    @PostMapping("/drivers/register")
    public ApiResponseDto<DriverRegisterResponseDto> registerDriver(@RequestBody RegisterDriverRequestDto registerDriverRequestDto) {
        DriverRegisterResponseDto driverRegisterResponseDto = null;
        try {
            logger.info("Received driver registration request for: {}", registerDriverRequestDto.getName());
            driverRegisterResponseDto = authenticationService.driverRegisterRequest(registerDriverRequestDto);
            logger.info("Driver registration successful for: {}", registerDriverRequestDto.getName());
            return ApiResponseDto.statusOk(driverRegisterResponseDto);
        } catch (DatabaseException e) {
            logger.error("Error occurred while registering driver: {}. Error: {}", registerDriverRequestDto.getName(), e.getMessage(), e);
            return ApiResponseDto.statusInternalServerError(driverRegisterResponseDto, e);
        }
    }

    /**
     * <p>
     * This method is responsible for Authentication. If authentication fails it will throw
     * certain type of exceptions and Http status code accordingly.
     * </p>
     *
     * @param authenticationRequestDto {@link AuthenticationRequestDto}
     *                                 This dto must contain fields of AuthenticationRequestDto.
     * @return ApiResponseDto<AuthenticationResponse>
     * @throws NotFoundException       Thrown if the username(MOBILE NUMBER) is not found in the database.
     * @throws AuthenticationException Thrown if the authentication failed with bad credentials.
     */
    @PostMapping
    public ApiResponseDto<AuthenticationResponseDto> authentication(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        AuthenticationResponseDto authenticationResponse = null;
        try {
            authenticationResponse = authenticationService.authenticate(authenticationRequestDto);
        } catch (NotFoundException e) {
            logger.error("No user with mobileNumber " + authenticationRequestDto.getMobileNumber() +
                    " is found in database");
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusNoContent(authenticationResponse);
        } catch (AuthenticationException e) {
            logger.error("Invalid credentials given by the user");
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusUnAuthorized(authenticationResponse, e);
        }
        logger.info("User authenticated successfully!");
        return ApiResponseDto.statusOk(authenticationResponse);
    }
}