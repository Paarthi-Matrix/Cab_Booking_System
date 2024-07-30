package com.i2i.zapcab.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.model.User;

/**
 * <p>
 * An interface that manages all the user related service
 * </p>
 */
@Component
public interface UserService {

    /**
     * <p>
     * Retrieves the user by userId
     * </p>
     *
     * @param id User id of the user.
     * @return Optional<User> {@link User}
     */
    Optional<User> getUserById(String id);

    /**
     * <p>
     * This method is used to get the user based on their mobile number.
     * </p>
     *
     * @param mobileNumber Mobile number of the User.
     * @return User {@link  User}
     * @throws com.i2i.zapcab.exception.DatabaseException Arises when there is read anomalies in user database.
     */
    User getUserByMobileNumber(String mobileNumber);

    /**
     * <p>
     * This method is used to save the user to the database.
     * </p>
     *
     * @param user {@link User}
     * @throws com.i2i.zapcab.exception.DatabaseException Arises when there is create anomalies in user database.
     */
    void saveUsers(User user);

    /**
     * <p>
     * This method is used to change the password for both the driver and customer
     * </p>
     *
     * @param id          UserId of the user.
     * @param newPassword New password of the user.
     * @throws com.i2i.zapcab.exception.DatabaseException Arises when there is update anomalies in user database.
     */
    void changePassword(String id, String newPassword);

    /**
     * <p>
     * Checks if the user is soft deleted from the database.
     * This make us ensure that the user cannot access the application after their account been deleted,
     * even though the user have a valid JWT token.
     * </p>
     *
     * @param username User_id of the user (Customer or Driver).
     * @return boolean
     * Returns false when user is not softly deleted. Else returns true.
     */
    boolean checkIfUserSoftDeleted(String username);

    /**
     * <p>
     * Masks the user's mobile number according to their wish
     * </p>
     *
     * @param id   User's unique id
     * @param mask
     * @return MaskMobileNumberResponseDto
     * A successful message if the mobile number is masked otherwise
     * @throws com.i2i.zapcab.exception.DatabaseException Arises when there is update anomalies in user database.
     */
    MaskMobileNumberResponseDto updateMaskMobileNumber(String id, boolean mask);

    /**
     * <p>
     * Marks a user as deleted by setting the deleted flag to true.
     * </p>
     *
     * @param id The ID of the user to be marked as deleted.
     * @return boolean - true if the user is successfully marked as deleted, false otherwise.
     * @throws com.i2i.zapcab.exception.DatabaseException Arises when there is delete anomalies in user database.
     */
    boolean deleteById(String id);
}