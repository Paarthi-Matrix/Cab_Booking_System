package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/admins")
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * This method is used to get the pending request who are yet to be verified.
     * @return List
     *      Returns the list of requests pending along with the HTTP status code.
     */
    @GetMapping
    public ResponseEntity<List<PendingRequest>> pendingProcessing(){
        return ResponseEntity.ok(adminService.getAllPendingRequests());
    }

    /**
     * This method is used to update the pending requests after the user has been verified.
     * @param updatePendingRequestDto {@link UpdatePendingRequestDto}
     * @return ResponseEntity
     *      Returns a message for the successful updation along with HTTP ok status.
     */
    @PutMapping
    public ResponseEntity<AuthenticationResponse> updatePendingRequests( @RequestBody UpdatePendingRequestDto updatePendingRequestDto) {
        return ResponseEntity.ok(adminService.modifyPendingRequest(updatePendingRequestDto));
    }

}
