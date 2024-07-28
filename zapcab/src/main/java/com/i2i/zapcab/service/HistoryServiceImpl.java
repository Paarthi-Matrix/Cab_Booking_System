package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.RideHistoryResponseDto;
import com.i2i.zapcab.dto.TierDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.mapper.HistoryMapper;
import com.i2i.zapcab.model.History;
import com.i2i.zapcab.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.ArrayList;

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
            throw new DatabaseException("Error Occurred while fetching" +
                    " all rides associated to user: " + id, e);
        }
    }

    public void saveHistory(History history) {
        try {
            historyRepository.save(history);
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while saving" +
                    " ride history of user: " + history.getUser().getId(), e);
        }
    }

    @Override
    public TierDto getCustomerTier(String userId) {
        try {
            int rideCount = historyRepository.countByUserId(userId);
            String newTier = determineTier(rideCount);
            TierDto tierDto = TierDto.builder()
                    .tier(newTier)
                    .build();
            customerService.updateCustomerTier(userId, tierDto);
            return tierDto;
        } catch (Exception e) {
            throw new DatabaseException("Error updating customer tier for userId: " + userId, e);
        }
    }

    /**
     * <p>
     * Determines the tier of the customer based on the number of rides they have taken.
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