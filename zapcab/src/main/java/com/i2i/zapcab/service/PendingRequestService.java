package com.i2i.zapcab.service;

import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface PendingRequestService {
    Page<PendingRequest> getAllPendingRequests(int page, int size);

    public Optional<PendingRequest> findRequestByMobileNumber(String mobileNumber);

    public PendingRequest saveRequests(PendingRequest pendingRequest);
}
