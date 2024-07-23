package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.model.PendingRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminService {
    /**
     * Used to get the list of pending requests which needs for background verification
     * @return List
     *     Which holds the requested list
     */
    public List<PendingRequest> getAllPendingRequests();
    /**
     * Helps to update the pending requests either the admin approve or rejects the application
     * @param updatePendingRequestDto {@link UpdatePendingRequestDto}
     * @return A message on successful updation
     */
    public AuthenticationResponse modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto);
}
