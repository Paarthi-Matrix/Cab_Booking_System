package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.mapper.HistoryMapper;
import com.i2i.zapcab.model.History;
import com.i2i.zapcab.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Service implementation for managing customer history related operations.
 * </p>
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private CustomerService customerService;

    private HistoryMapper historyMapper = new HistoryMapper();

    public List<RideHistoryResponseDto> getAllRideHistoryById(String id) {
        try {
            List<RideHistoryResponseDto> rideHistoryResponseDtos = new ArrayList<>();
            historyRepository.findAllByUserId(id).
                    forEach((history) -> rideHistoryResponseDtos.add(historyMapper.entityToDto(history)));
            return rideHistoryResponseDtos;
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while fetching" +
                    " all rides associated to user: " + id, e);
        }
    }

    public void saveHistory(History history) {
        try {
            historyRepository.save(history);
        } catch (Exception e) {
            throw new UnexpectedException("Error Occurred while saving" +
                    " ride history of user: " + history.getUser().getId(), e);
        }
    }

    /**
     * <p>
     * Updates the tier of a customer based on the user ID.
     * </p>
     *
     * @param userId The userId of the customer.
     * @return TierDto
     * The updated tier information of the customer.
     */
    @Override
    public TierDto updateCustomerTier(String userId) {
        try {
            int rideCount = 20;
            String newTier = determineTier(rideCount);
            customerService.updateCustomerTier(userId, newTier);
            return TierDto.builder().tier(newTier).build();
        } catch (Exception e) {
            throw new UnexpectedException("Error updating customer tier for userId: " + userId, e);
        }
    }

    /**
     * <p>
     * Determines the tier of the customer based on the number of rides they have taken.
     * </p>
     *
     * @param rideCount The number of rides of customer has taken.
     * @return String
     * The tier of the customer.
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