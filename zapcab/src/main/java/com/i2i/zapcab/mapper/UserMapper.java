package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.RegisterUserRequestDto;
import com.i2i.zapcab.dto.UserResponseDto;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.model.User;

import java.util.List;

public class UserMapper {
    public User RequestDtoToEntity(RegisterUserRequestDto registerUserRequestDto, List<Role> roles) {
        return User.builder().name(registerUserRequestDto.getName()).
                mobileNumber(registerUserRequestDto.getMobileNumber())
                .email(registerUserRequestDto.getEmail()).
                gender(registerUserRequestDto.getGender()).
                dateOfBirth(registerUserRequestDto.getDateOfBirth()).
                password(registerUserRequestDto.getPassword()).role(roles).build();
    }
}
