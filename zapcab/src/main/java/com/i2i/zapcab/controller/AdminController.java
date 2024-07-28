package com.i2i.zapcab.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.service.AdminService;

@RestController
@RequestMapping("/v1/admins")
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    @Autowired
    private AdminService adminService;
    /**
     * <p>
     *     This method is responsible for fetching the pending requests for the driver,
     *     which is used by admin to approve or reject the driver's request to ride or work as
     *     ZapCab driver/rider.
     * </p>
     * <ul>
     *     <li>This method uses the pagination concept.</li>
     *     <li>Provide proper page and size for the pagination</li>
     * </ul>
     * @param page
     *        Page to be fetched.
     * @param size
     *        Number of rows to be fetched.
     * @return ApiResponseDto<Page<FetchAllPendingRequestsDto>> {@link FetchAllPendingRequestsDto}
     */
    @GetMapping
    public ApiResponseDto<Page<FetchAllPendingRequestsDto>> fetchAllPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.debug("Entering into the method to fetch all the requests..");
        Page<FetchAllPendingRequestsDto> fetchAllPendingRequestsDto = adminService.getAllPendingRequest(page, size);
        try {
            logger.info("Fetching all the requests");
            return ApiResponseDto.statusOk(fetchAllPendingRequestsDto);
        } catch (UnexpectedException e) {
            logger.error("Error occurred while fetching the pending requests ");
            return ApiResponseDto.statusInternalServerError(fetchAllPendingRequestsDto, e);
        }
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
     * @param updatePendingRequestDto {@link UpdatePendingRequestDto}
     * @return
     */
    @PutMapping
    public ApiResponseDto<AuthenticationResponseDto> updatePendingRequest(@Valid @RequestBody UpdatePendingRequestDto
                                                                                      updatePendingRequestDto) {
        logger.debug("Entering into the method to update the particular request...");
        AuthenticationResponseDto authenticationResponse = null;
        try {
            logger.info("Updating the request for the user : {}", updatePendingRequestDto.getPhoneNumber());
            authenticationResponse = adminService.updatePendingRequest(updatePendingRequestDto);
            logger.info("Successfully updated the {} user's status ", updatePendingRequestDto.getPhoneNumber());
        } catch (UnexpectedException e) {
           return ApiResponseDto.statusInternalServerError(authenticationResponse, e);
        }
        logger.debug("leaving the method  updateRequest ");
        return ApiResponseDto.statusOk(authenticationResponse);
    }
}
