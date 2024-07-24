package com.i2i.zapcab.service;


import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerRequestDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.dto.RegisterUserRequestDto;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     The AuthenticationService interface is responsible for defining the
 *     authentication for sign up functionality for driver, customer and authenticate user(customer or driver)
 * </p>
 */
@Component
public interface AuthenticationService {
    /**
     * This method is used to register the user as customer.
     * @param registerRequestDto {@link RegisterUserRequestDto}
     * @return Token
     *       Returns the token as string for each user
     */
    public AuthenticationResponseDto customerRegister(RegisterCustomerRequestDto registerRequestDto);

    /**
     * This method is used to authenticate the user
     * @param authenticationRequestDto
     * @return
     */
    public AuthenticationResponseDto authenticate (AuthenticationRequestDto authenticationRequestDto);

    DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto);
}
