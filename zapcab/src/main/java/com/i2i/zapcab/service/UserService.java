package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <p>
 *     An interface that manages all the user related service
 * </p>
 */
@Component
public interface UserService {

    Optional<User> getUserById(String id);

    User getUserByMobileNumber(String mobileNumber);

    void saveUsers(User user);

    void changePassword(String id, String newPassword);

    /**
     * <p>
     *     Masks the user's mobile number according to their wish
     * </p>
     * @param id
     *      User's unique id
     * @param mask
     * @return MaskMobileNumberResponseDto
     *      A successful message if the mobile number is masked otherwise
     */
    MaskMobileNumberResponseDto updateMaskMobileNumber(String id, boolean mask);
}
