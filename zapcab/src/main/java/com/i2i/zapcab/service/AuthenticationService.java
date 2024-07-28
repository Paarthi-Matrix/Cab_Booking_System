package com.i2i.zapcab.service;


import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
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
     * @param registerCustomerDto {@link RegisterCustomerDto}
     * @return Token
     *       Returns the token as string for each user
     */
    public AuthenticationResponseDto customerRegister(RegisterCustomerDto registerCustomerDto);

    /**
     * This method is used to authenticate the user
     * @param authenticationRequestDto
     * @return
     */
    public AuthenticationResponseDto authenticate (AuthenticationRequestDto authenticationRequestDto);

    DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto);
}
