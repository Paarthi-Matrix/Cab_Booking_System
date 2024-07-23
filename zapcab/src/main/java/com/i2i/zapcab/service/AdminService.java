package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface AdminService {
    Page<PendingRequest> pendingRequestProcessing(int page, int size);
    AuthenticationResponseDto modifyPendingRequest(UpdatePendingRequestDto updatePendingRequestDto);
}