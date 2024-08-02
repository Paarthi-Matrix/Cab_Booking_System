package com.i2i.zapcab.mapper;

import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import com.i2i.zapcab.model.User;

import java.util.List;

import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_REMARKS;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_STATUS_OF_DRIVER;

public class AuthenticationMapper {
    public AuthenticationResponseDto tokenToResponseToken(String token) {
        return AuthenticationResponseDto.builder().token(token).build();
    }

    public User toUser(RegisterCustomerDto registerCustomerDto, List<Role> roles) {
        return  User.builder()
                .name(registerCustomerDto.getName())
                .dateOfBirth(registerCustomerDto.getDateOfBirth())
                .email(registerCustomerDto.getEmail())
                .gender(registerCustomerDto.getGender())
                .mobileNumber(registerCustomerDto.getMobileNumber())
                .password(registerCustomerDto.getPassword())
                .role(roles)
                .build();
    }

    public PendingRequest toPendingRequest(RegisterDriverRequestDto registerDriverRequestDto) {
        return PendingRequest.builder()
                .name(registerDriverRequestDto.getName())
                .email(registerDriverRequestDto.getEmail())
                .region(registerDriverRequestDto.getRegion())
                .licenseNo(registerDriverRequestDto.getLicenseNumber())
                .rcBookNo(registerDriverRequestDto.getRcBookNo())
                .status(INITIAL_STATUS_OF_DRIVER)
                .city(registerDriverRequestDto.getCity())
                .dob(registerDriverRequestDto.getDateOfBirth())
                .gender(registerDriverRequestDto.getGender())
                .mobileNumber(registerDriverRequestDto.getMobileNumber())
                .category(registerDriverRequestDto.getCategory())
                .model(registerDriverRequestDto.getModel())
                .type(registerDriverRequestDto.getType())
                .remarks(INITIAL_REMARKS)
                .licensePlate(registerDriverRequestDto.getLicensePlate())
                .build();
    }
}