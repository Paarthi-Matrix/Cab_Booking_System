package com.i2i.zapcab.service;

import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface PendingRequestService {
    void savePendingRequest(PendingRequest pendingRequest);
    Page<PendingRequest> getAllPendingRequests(int page, int size);
}
