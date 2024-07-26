package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.TierDto;
import org.springframework.stereotype.Component;

@Component
public interface HistoryService {

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
