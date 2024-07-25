package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.exception.ServerException;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.service.AdminService;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/admins")
public class AdminController {

    @Autowired
    AdminService adminService;

    /**
     * <p>
     *     This method is responsible for fetching the pending requests for the driver,
     *     which is used by admin to approve or reject the driver's request to ride or work as
     *     ZapCab driver/rider.
     * </p>
     * <p>
     *     NOTE:
     * </p>
     * <ul>
     *     <li>This method uses the pagination concept.</li>
     *     <li>Provide proper page and size for the pagination</li>
     * </ul>
     * @param page
     *        Page to be fetched.
     * @param size
     *        Number of rows to be fetched.
     * @return ApiResponseDto<Page<FetchAllPendingRequestsDto>>
     */
    @GetMapping
    public ApiResponseDto<Page<FetchAllPendingRequestsDto>> pendingProcessing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponseDto.statusOk(adminService.pendingRequestProcessing(page, size));
    }

    /**
     * <p>
     *   This method is responsible for updating the pending request of the driver.
     *   The driver is added or removed from the organization according to following act.
     * </p>
     * <ul>
     *     <li>If the driver's personal information seems to have anomalies.</li>
     *     <li>If the driver's back ground verification was not fair enough.</li>
     * </ul>
     * @param updatePendingRequestDto
     * @return
     */
    @PutMapping
    public ApiResponseDto<AuthenticationResponseDto> updatePendingRequests(@RequestBody UpdatePendingRequestDto updatePendingRequestDto) {
        AuthenticationResponseDto authenticationResponse = null;
        try {
            authenticationResponse = adminService.modifyPendingRequest(updatePendingRequestDto);
        } catch (UnexpectedException e) {
           return ApiResponseDto.statusInternalServerError(authenticationResponse, e);
        }
        return ApiResponseDto.statusOk(authenticationResponse);
    }

}
