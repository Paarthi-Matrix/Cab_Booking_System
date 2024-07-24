package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.repository.PendingRequestRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PendingRequestServiceImpl implements PendingRequestService {
    @Autowired
    PendingRequestRepository pendingRequestRepository;

    @Transactional
    @Override
    public PendingRequest savePendingRequest(PendingRequest pendingRequest) {
        try {
            return pendingRequestRepository.save(pendingRequest);
        } catch (Exception e) {
            String errorMessage = "Un expected error happened while updating " +
                    pendingRequest.getName() +
                    " to the pending requests";
            throw new UnexpectedException(errorMessage, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PendingRequest> getAllPendingRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pendingRequestRepository.findAll(pageable);
    }
    @Override
    public Optional<PendingRequest> findRequestByMobileNumber(String mobileNumber) {
        return pendingRequestRepository.findByMobileNumber(mobileNumber);
    }
}
