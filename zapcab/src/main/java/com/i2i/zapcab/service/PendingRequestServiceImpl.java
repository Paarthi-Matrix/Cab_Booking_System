package com.i2i.zapcab.service;

import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.PendingRequest;
import com.i2i.zapcab.repository.PendingRequestRepository;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *     Implements {@link PendingRequestService}
 *     Provides the business logic to perform operations like fetching, finding the requests.
 * </p>
 */
@Service
public class PendingRequestServiceImpl implements PendingRequestService {
    private static final Logger logger = LogManager.getLogger(PendingRequestServiceImpl.class);
    @Autowired
    private PendingRequestRepository pendingRequestRepository;

    @Transactional
    @Override
    public void savePendingRequest(PendingRequest pendingRequest) {
        try {
            logger.info("Saving the request made by the user {}", pendingRequest.getName());
             pendingRequestRepository.save(pendingRequest);
        } catch (Exception e) {
            logger.error("Unable to save the request for {}", pendingRequest.getName());
            String errorMessage = "Un expected error happened while updating " +
                    pendingRequest.getName() +
                    " to the pending requests";
            throw new DatabaseException(errorMessage, e);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Page<PendingRequest> getAllPendingRequests(int page, int size) {
        logger.debug("Entering into the method to fetch the requests....");
        try {
            Pageable pageable = PageRequest.of(page, size);
            logger.info("Successfully retrieved all the request in a page {}", pageable.getPageNumber());
            return pendingRequestRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error occurred while fetching the request for the page {} ", page);
            throw new DatabaseException("Unable to retrieve the pending request in a page : "+page, e);
        }
    }
    @Override
    public Optional<PendingRequest> findRequestByMobileNumber(String mobileNumber) {
        logger.debug("Entering into the method to find the request......");
        try {
            return pendingRequestRepository.findByMobileNumber(mobileNumber);
        } catch (Exception e) {
            logger.error("Unable to get the request for the given mobile number {}", mobileNumber, e);
            throw new DatabaseException("Un excepted error arise while finding the request ", e);
        }
    }
}
