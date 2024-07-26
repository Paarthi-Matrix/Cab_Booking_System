package com.i2i.zapcab.service;

import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <p>
 *    An interface that handles the requested that are made by the user.
 * </p>
 */
@Component
public interface PendingRequestService {
    /**
     * <p>
     *     This method is used to save the pending request provided by the driver
     * </p>
     * @param pendingRequest {@link PendingRequest}
     */
    public void savePendingRequest(PendingRequest pendingRequest);

    /**
     * <p>
     *     Retrieves all the pending requests
     * </p>
     * @param page
     *       Page number that to be fetched
     * @param size
     *       No of rows that are to be displayed in a page
     * @return PendingRequests
     */
    Page<PendingRequest> getAllPendingRequests(int page, int size);

    /**
     * <p>
     *     This method is used to find the pending request by providing driver mobile number.
     * </p>
     * @param mobileNumber
     *       Customer's mobile number
     * @return PendingRequest
     *          Returns the data if present else return null
     */
    public Optional<PendingRequest> findRequestByMobileNumber(String mobileNumber);

}
