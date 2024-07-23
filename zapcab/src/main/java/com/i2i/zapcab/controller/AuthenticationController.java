package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.*;
import com.i2i.zapcab.exception.AuthenticationException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
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

    /**
     * <p>
     *     This method is responsible for registering customer (Customer Sign up).
     * </p>
     * @param registerCustomerRequestDto {@link RegisterCustomerRequestDto}
     *        The request body must contain all the fields of RegisterCustomerRequestDto.
     *
     * @return ApiResponseDto<AuthenticationResponse>
     */
    @PostMapping("/customers/register")
    public ApiResponseDto<AuthenticationResponseDto> registerCustomer(@RequestBody RegisterCustomerRequestDto registerCustomerRequestDto) {
        return ApiResponseDto.statusCreated(authenticationService.customerRegister(registerCustomerRequestDto));
    }

    /**
     * <p>
     *     This method is responsible for registering driver.
     * </p>
     * @param `RegisterDriverDto` {@link DriverRegisterResponseDto}
     *         The request body must contain all the fields of the RegisterDriverDto.
     * @return ApiResponseDto<DriverRegisterRequestResponseDto>
     */
    @PostMapping("/drivers/register")
    public ApiResponseDto<DriverRegisterResponseDto> registerDriver(@RequestBody RegisterDriverRequestDto registerDriverRequestDto){
        System.out.println("/register/driver");
       return ApiResponseDto.statusCreated(authenticationService.driverRegisterRequest(registerDriverRequestDto));
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
    public ApiResponseDto<AuthenticationResponseDto> authentication(@RequestBody AuthenticationRequestDto authenticationRequsetDto) {
        AuthenticationResponseDto authenticationResponse = null;
        try {
            authenticationResponse = authenticationService.authenticate(authenticationRequsetDto);
        } catch (NotFoundException e) {
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusNoContent(authenticationResponse, e);
        } catch (AuthenticationException e) {
            authenticationResponse = AuthenticationResponseDto.builder().token("").build();
            return ApiResponseDto.statusUnAuthorized(authenticationResponse,e);
        }
        return ApiResponseDto.statusOk(authenticationResponse);
    }
}
