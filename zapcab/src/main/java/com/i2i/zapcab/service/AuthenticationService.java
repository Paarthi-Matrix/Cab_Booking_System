package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.common.ZapCabConstant;
import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.DriverRegisterResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.dto.RegisterCustomerRequestDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.exception.DatabaseException;

/**
 * <p>
 * The AuthenticationService interface is responsible for defining the
 * authentication for sign up functionality for driver, customer and authenticate user(customer or driver)
 * </p>
 */
@Component
public interface AuthenticationService {
    /**
     * <p>
     * The `CustomerRegister` is responsible for registering the customer with ROLE_CUSTOMER.
     * The customer tire is initially set to Bronze. Their tire is updated according to
     * their history and frequency of rides. The INITIAL_CUSTOMER_TIRE is set in constants.
     * </p>
     * <p>
     * Also Refer {@link ZapCabConstant}
     * </p>
     *
     * @param registerRequestDto {@link RegisterDriverRequestDto}
     * @return AuthenticationResponseDto
     * Contains JWT token upon successfull registration.
     * @throws DatabaseException Arises while saving/updating the entity to the database.
     */
    AuthenticationResponseDto customerRegister(RegisterCustomerDto registerRequestDto);

    /**
     * <p>
     * This method is used for authentication of both driver and customer.
     * The user can use mobile number and password to sign in.
     * Upon successful authentication a JWT token is returned.
     * </p>
     *
     * @param authenticationRequestDto {@link AuthenticationRequestDto}
     * @return AuthenticationResponseDto {@link AuthenticationResponseDto}
     */
    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    /**
     * <p>
     * This method is responsible for registering driver to the database.
     * The drive cannot register as driver by themselves, due to some security constrains.
     * Due to which
     * </p>
     *
     * @param registerDriverRequestDto {@link RegisterCustomerRequestDto}
     * @return DriverRegisterResponseDto {@link DriverRegisterResponseDto}
     */
    DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto);
}
