package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.service.AuthenticationServiceImpl;
import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     The AuthenticationController class handles all authentication-related requests for the application.
 *     It includes endpoints for registering customers and drivers, as well as authenticating users.
 * </p>
 * <p>
 *     This controller is responsible for processing the registration and authentication requests.
 *     It ensures that the input data is received correctly,
 *     processes the data through the service layer, and returns appropriate responses to the client.
 * </p>
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    /**
     * <p>
     *     This method is responsible for registering customer (Customer Sign up).
     * </p>
     * @param registerCustomerDto {@link RegisterCustomerDto}
     *        The request body must contain all the fields of RegisterCustomerRequestDto.
     *
     * @return ApiResponseDto<AuthenticationResponse> {@link ApiResponseDto}
     *         Returns the JWT Token with 201 CREATED upon successful registration .
     *         Else returns 500 INTERNAL_SERVER_ERROR.
     */
    @PostMapping("/customers/register")
    public ApiResponseDto<AuthenticationResponseDto> registerCustomer(@Valid @RequestBody RegisterCustomerDto registerCustomerDto) {
        AuthenticationResponseDto authenticationResponseDto = null;
        try {
            authenticationResponseDto = authenticationService.customerRegister(registerCustomerDto);
        } catch (UnexpectedException e) {
            return ApiResponseDto.statusInternalServerError(authenticationResponseDto,e);
        }
        logger.info("Customer successfully created!.");
        return ApiResponseDto.statusCreated(authenticationResponseDto);
    }

    /**
     * <p>
     *     This method is responsible for registering driver.
     * </p>
     * @param  registerDriverRequestDto {@link DriverRegisterResponseDto}
     *         The request body must contain all the fields of the RegisterDriverDto.
     * @return ApiResponseDto<DriverRegisterRequestResponseDto> {@link ApiResponseDto}
     *         Returns the JWT Token with 201 CREATED upon successful registration .
     *        Else returns 500 INTERNAL_SERVER_ERROR.
     */
    @PostMapping("/drivers/register")
    public ApiResponseDto<DriverRegisterResponseDto> registerDriver(@Valid @RequestBody RegisterDriverRequestDto registerDriverRequestDto){
        DriverRegisterResponseDto driverRegisterResponseDto = null;
       try {
           driverRegisterResponseDto = authenticationService.driverRegisterRequest(registerDriverRequestDto);
       } catch (UnexpectedException e) {
           return ApiResponseDto.statusInternalServerError(driverRegisterResponseDto, e);
       }
       logger.info("Driver successfully added to pending request!.");
       return ApiResponseDto.statusOk(driverRegisterResponseDto);
    }

    /**
     * <p>
     *     This method is responsible for Authentication. If authentication fails it will throw
     *     certain type of exceptions and Http status code accordingly.
     * </p>
     *
     * @param `AuthenticationRequestDto` {@link AuthenticationRequestDto}
     *         This dto must contain fields of AuthenticationRequestDto.
     * @throws NotFoundException
     *         Thrown if the username(MOBILE NUMBER) is not found in the database.
     * @throws AuthenticationException
     *         Thrown if the authentication failed with bad credentials.
     * @return ApiResponseDto<AuthenticationResponse>
     *
     */
    @PostMapping
    public ApiResponseDto<AuthenticationResponseDto> authentication(@Valid @RequestBody AuthenticationRequestDto authenticationRequsetDto) {
        AuthenticationResponseDto authenticationResponse = null;
        try {
            authenticationResponse = authenticationService.authenticate(authenticationRequsetDto);
        } catch (NotFoundException e) {
            logger.debug("No user with mobileNumber " + authenticationRequsetDto.getMobileNumber() +
                    " is found in database");
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusNoContent(authenticationResponse);
        } catch (AuthenticationException e) {
            logger.debug("Invalid credentials given by the user");
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusUnAuthorized(authenticationResponse,e);
        }
        logger.info("User authenticated successfully!");
        return ApiResponseDto.statusOk(authenticationResponse);
    }
}
