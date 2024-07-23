package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.AuthenticationResponse;

public class AuthenticationMapper {
    public AuthenticationResponse tokenToResponseToken(String token){
        return AuthenticationResponse.builder().token(token).build();
    }
}
