package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     An interface for managing the user's details by admin
 *     Defines methods to handle the user's requests and the updates the requests based on the
 *     results from background verification
 * </p>
 */
import java.util.List;

@Component
public interface AdminService {
    /**
     * <p>
     * This method is usd to retrieve all the pending requests that are made by the user for driver position.
     * </p>
     *
     * @param page Page number to be fetched
     * @param size No of rows to be fetched
     * @return List
     *        holds all the requests
     * @throws AuthenticationException
     *       throws whenever authentication get failed
     */
    Page<FetchAllPendingRequestsDto> getAllPendingRequest(int page, int size);

    /**
     * <p>
     * Updates the requests by the admin with background verification made for each user
     * with their license and rc book
     * </p>
     *
     * @param updatePendingRequestDto {@link UpdatePendingRequestDto}
     * @return AuthenticationResponse {@link AuthenticationResponseDto}
     * @throws AuthenticationException
     *          Whenever updating get failed
     */
    public AuthenticationResponseDto updatePendingRequest(UpdatePendingRequestDto updatePendingRequestDto);
}
