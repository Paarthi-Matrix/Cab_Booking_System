package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.AuthenticationResponseDto;

public class AuthenticationMapper {
    public AuthenticationResponseDto tokenToResponseToken(String token) {
        return AuthenticationResponseDto.builder().token(token).build();
    }
}
