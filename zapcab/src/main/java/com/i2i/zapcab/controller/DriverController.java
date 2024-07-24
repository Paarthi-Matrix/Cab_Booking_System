package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.AvailableDriverDto;
import com.i2i.zapcab.dto.DriverSelectedRideDto;
import com.i2i.zapcab.dto.GetRideRequestListsDto;
import com.i2i.zapcab.dto.RequestedRideDto;
import com.i2i.zapcab.dto.RideDetailsDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.ChangePasswordRequestDto;
import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.helper.JwtDecoder;
import com.i2i.zapcab.service.DriverService;

/**
 * <p>
 *     The DriverController class handles HTTP requests related to driver operations.
 *     This includes updating the driver's status and location, and changing the driver's password.
 * </p>
 * <p>
 *     The methods in this controller return ApiResponseDto objects, which encapsulate the response
 *     data and status code for the API responses.
 * </p>
 */
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
     * @param updateDriverStatusDto {@link UpdateDriverStatusDto}
     *       This must contain all the values of the `UpdateDriverStatusDto`.
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me/locations")
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
     *<p>
     *     This method is used to display the requested ride to the driver. The filtration is done based on
     *      location and the vehicle category.
     *</p>
     * @param getRideRequestListsDto {@link GetRideRequestListsDto}
     * @return ApiResponseDto<List<RequestedRideDto> {@link RequestedRideDto}
     */
   @GetMapping("me/requests")
    public ApiResponseDto<List<RequestedRideDto>> getAvailableDrivers(@RequestBody GetRideRequestListsDto getRideRequestListsDto ){
       List<RequestedRideDto> requestedRideDtos = driverService.getRideRequests(getRideRequestListsDto);
       return ApiResponseDto.statusOk(requestedRideDtos);
   }

    /**
     * <p>
     *     This method is responsible for changing the password of the driver.
     * </p>
     * @param ChangePasswordRequestDto {@link ChangePasswordRequestDto}
     * @return ApiResponseDto<String> {@link ApiResponseDto}
     */
    @PatchMapping("/me/password")
    public ApiResponseDto<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        String id = JwtDecoder.extractUserIdFromToken();
        driverService.changePassword(id, changePasswordRequestDto);
        return ApiResponseDto.statusOk("Driver password changed successfully!");
    }

    /**
     * <p>
     *     This method is used to fetch the ride details mapped with the customer.
     * </p>
     * @param selectedRideDto {@link DriverSelectedRideDto}
     * @return
     */
   @PostMapping("/request/accept")
    public ApiResponseDto<RideDetailsDto> getRideDetails(@RequestBody DriverSelectedRideDto selectedRideDto){
       RideDetailsDto rideDetailsDto = driverService.getRideDetails(selectedRideDto);
       return ApiResponseDto.statusOk(rideDetailsDto);
   }

}
