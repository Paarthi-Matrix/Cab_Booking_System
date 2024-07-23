package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.*;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {
    AuthenticationResponseDto customerRegister(RegisterCustomerRequestDto registerRequestDto);
    AuthenticationResponseDto authenticate (AuthenticationRequestDto authenticationRequestDto);
    DriverRegisterResponseDto driverRegisterRequest(RegisterDriverRequestDto registerDriverRequestDto);
}
