package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.RegisterUserRequestDto;
import com.i2i.zapcab.dto.RegisterDriverDto;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {

    /**
     * This method is used to register the user as customer.
     * @param registerRequestDto {@link RegisterDriverDto}
     * @return Token
     *       Returns the token as string for each user
     */
    public AuthenticationResponse customerRegister(RegisterUserRequestDto registerRequestDto);

    /**
     * This method is used to authenticate the user
     * @param authenticationRequestDto
     * @return
     */
    public AuthenticationResponse authenticate (AuthenticationRequestDto authenticationRequestDto);

    public AuthenticationResponse adminRegister(RegisterUserRequestDto registerRequestDto);

    public AuthenticationResponse driverRequest(RegisterDriverDto registerDriverDto);

}
