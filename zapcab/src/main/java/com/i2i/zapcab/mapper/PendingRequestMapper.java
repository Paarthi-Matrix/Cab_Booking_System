package com.i2i.zapcab.mapper;

import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_REMARKS;
import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_STATUS_OF_DRIVER;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.RegisterDriverRequestDto;
import com.i2i.zapcab.model.PendingRequest;

public class PendingRequestMapper {
    public PendingRequest dtoToEntity(RegisterDriverRequestDto registerDriverDto) {
        return PendingRequest.builder().
                name(registerDriverDto.getName()).
                email(registerDriverDto.getEmail()).
                mobileNumber(registerDriverDto.getMobileNumber()).
                region(registerDriverDto.getRegion()).
                city(registerDriverDto.getCity()).
                dob(registerDriverDto.getDateOfBirth()).
                gender(registerDriverDto.getGender()).
                category(registerDriverDto.getCategory()).
                model(registerDriverDto.getModel()).
                type(registerDriverDto.getType()).
                status(INITIAL_STATUS_OF_DRIVER).
                remarks(INITIAL_REMARKS).
                licensePlate(registerDriverDto.getLicensePlate()).
                licenseNo(registerDriverDto.getLicenseNumber()).
                rcBookNo(registerDriverDto.getRcBookNo()).build();
    }

    public RegisterDriverRequestDto entityToDto(PendingRequest pendingRequest) {
        return RegisterDriverRequestDto.builder().
                name(pendingRequest.getName()).
                email(pendingRequest.getEmail()).
                mobileNumber(pendingRequest.getMobileNumber()).
                region(pendingRequest.getRegion()).
                city(pendingRequest.getCity()).
                dateOfBirth(pendingRequest.getDob()).
                gender(pendingRequest.getGender()).
                category(pendingRequest.getCategory()).
                model(pendingRequest.getModel()).
                type(pendingRequest.getType()).
                licensePlate(pendingRequest.getLicensePlate()).
                licenseNumber(pendingRequest.getLicenseNo()).
                rcBookNo(pendingRequest.getRcBookNo()).build();
    }

    public FetchAllPendingRequestsDto entityToRequestDto(PendingRequest pendingRequest) {
        return FetchAllPendingRequestsDto.builder().
                name(pendingRequest.getName()).
                email(pendingRequest.getEmail()).
                mobileNumber(pendingRequest.getMobileNumber()).
                city(pendingRequest.getCity()).
                dob(pendingRequest.getDob()).
                category(pendingRequest.getCategory()).
                status(pendingRequest.getStatus()).
                remarks(pendingRequest.getRemarks()).
                model(pendingRequest.getModel()).
                licensePlate(pendingRequest.getLicensePlate()).
                licenseNo(pendingRequest.getLicenseNo()).
                rcBookNo(pendingRequest.getRcBookNo()).build();
    }
}