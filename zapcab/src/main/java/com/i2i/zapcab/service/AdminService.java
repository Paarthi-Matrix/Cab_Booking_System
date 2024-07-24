package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminService {
    /**
     * This method is usd to retrieve all the pending requests that are made by the user for driver position
     *
     * @return List holds all the requests
     */
    Page<PendingRequest> pendingRequestProcessing(int page, int size);

    /**
     * Updates the requests by the admin with background verification made for each user
     * with their license and rc book
     * @param updatePendingRequestDto {@link UpdatePendingRequestDto}
     * @return AuthenticationResponse {@link AuthenticationResponseDto}
     */
    public AuthenticationResponseDto modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto);
}
