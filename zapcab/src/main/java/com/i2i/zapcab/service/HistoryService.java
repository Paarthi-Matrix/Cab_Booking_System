package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.dto.TierDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *    An interface to maintain the operation that are related to the history entity
 *    This operation includes updating the customer tier, history maintenance
 * </p>
 */
@Component
public interface HistoryService {
    /**
     * <p>
     * Retrieves all ride history records for a specific user by their ID.
     * Converts the ride history entities to DTOs before returning them.
     * </p>
     *
     * @param id The ID of the user whose ride history is to be retrieved.
     * @return A list of RideHistoryResponseDto objects representing the user's ride history.
     * @throws UnexpectedException If an error occurs while fetching the ride history.
     */
    List<RideHistoryResponseDto> getAllRideHistoryById(String id);

    /**
     * <p>
     *     Updates the tier of a customer based on the user ID.
     * </p>
     * @param userId
     *        The userId of the customer.
     * @return TierDto
     *         The updated tier information of the customer.
     */
    public TierDto updateCustomerTier(String userId);
}
