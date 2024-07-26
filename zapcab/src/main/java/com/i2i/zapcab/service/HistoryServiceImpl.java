package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.exception.ZapCabException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     Service implementation for managing customer history related operations.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private final CustomerService customerService;

    /**
     * <p>
     *     Updates the tier of a customer based on the user ID.
     * </p>
     * @param userId
     *        The userId of the customer.
     * @return TierDto
     *         The updated tier information of the customer.
     */
    @Override
    public TierDto updateCustomerTier(String userId) {
        try {
            int rideCount = 20;
            String newTier = determineTier(rideCount);
            customerService.updateCustomerTier(userId, newTier);
            return TierDto.builder().tier(newTier).build();
        } catch (Exception e) {
            throw new ZapCabException("Error updating customer tier for userId: " + userId, e);
        }
    }

    /**
     * <p>
     *     Determines the tier of the customer based on the number of rides they have taken.
     * </p>
     * @param rideCount
     *        The number of rides of customer has taken.
     * @return String
     *         The tier of the customer.
     */
    private String determineTier(int rideCount) {
        if (rideCount > 50) {
            return "Platinum";
        } else if (rideCount > 30) {
            return "Gold";
        } else if (rideCount > 15) {
            return "Silver";
        } else {
            return "Bronze";
        }
    }
}
