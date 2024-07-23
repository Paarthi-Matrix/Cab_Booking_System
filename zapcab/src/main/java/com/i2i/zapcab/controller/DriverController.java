package com.i2i.zapcab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.ChangePasswordRequestDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.DriverService;

@RestController
@RequestMapping("/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    @Autowired
    DriverService driverService;

    /**
     * <p>
     *     This function is used to update the status and location of the driver.
     *     <p>
     *         NOTE : The status of the driver can be as follows,
     *     </p>
     *     <ol>
     *         <li>ONDUTY</li>
     *         <li>OFFDUTY</li>
     *         <li>SUSPENDED</li>
     *     </ol>
     * </p>
     * @param Userid
     *       This id will be available in the incoming JWT token.
     * @param updateDriverStatusDto {@link UpdateDriverStatusDto}
     *       This must contain all the values of the `UpdateDriverStatusDto`.
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me")
    public ApiResponseDto<String> updateDriverStatusAndLocation(@RequestBody UpdateDriverStatusDto updateDriverStatusDto) {
        String Userid = JwtDecoder.extractUserIdFromToken();
        try {
            driverService.updateDriverStatusAndLocation(Userid,updateDriverStatusDto);
        } catch (NotFoundException e) {
            return ApiResponseDto.statusNoContent("No such driver available", e);
        }
        return ApiResponseDto.statusOk("Driver location and status updated successfully!");
    }

    /**
     * <p>
     *     This method is responsible for changing the password of the driver.
     * </p>
     * @param `ChangePasswordRequestDto` {@link ChangePasswordRequestDto}
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me")
    public ApiResponseDto<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        driverService.changePassword(id, changePasswordRequestDto.getNewPassword());
        return ApiResponseDto.statusOk("Driver password changed successfully!");
    }
}
