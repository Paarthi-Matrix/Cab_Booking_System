package com.i2i.zapcab.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.UserRepository;

/**
 * <p>
 * Implements {@link UserService}
 * A service class that provides all the business logic for the following operations .
 *     <ol>
 *         <li> Changing password </li>
 *         <li> fetching user </li>
 *         <li> Masking the mobile number </li>
 *     </ol>
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUserByMobileNumber(String mobileNumber) {
        try {
            logger.info("Fetching the user details by providing mobile number {}", mobileNumber);
            return userRepository.findByMobileNumber(mobileNumber);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching the user for mobile number {}", mobileNumber);
            String errorMessage = "Unexpected error occurred while fetching the user for mobile number " + mobileNumber;
            throw new DatabaseException(errorMessage, e);
        }
    }

    @Override
    public void saveUsers(User user) {
        try {
            logger.info("Saving the user details for the user {}", user.getName());
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Un expected error happened while saving/updating user " +
                    user.getName(), e);
            String errorMessage = "Un expected error happened while saving/updating user " +
                    user.getName();
            throw new DatabaseException(errorMessage, e);
        }
    }

    @Override
    public void changePassword(String id, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        assert user != null;
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            logger.info("Updating the password for the user : {} ", user.getId(),"and their mobile " +
                            "number is", user.getMobileNumber());
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Un expected error happened while updating the new password for the user");
            String errorMessage = "Un expected error happened while updating the new password for the user";
            throw new DatabaseException(errorMessage, e);
        }
        logger.info("Password changed successfully!");
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, boolean mask) {
        try {
            logger.info("Masking the {} mobile number", id);
            User user = userRepository.findById(id).orElse(null);
            assert user != null;
            user.setMaskedMobileNumber(mask);
            logger.info("Successfully updated the value");
            userRepository.save(user);
            return MaskMobileNumberResponseDto.builder().message("Updated successfully").build();
        } catch (Exception e) {
            logger.error("Error while updating the user masking their mobile number", e.getMessage(), e);
            throw new DatabaseException("Unable to update the user's wish", e);
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                User updatedUser = user.get();
                updatedUser.setDeleted(true);
                return !ObjectUtils.isEmpty(userRepository.save(updatedUser));
            }
        } catch (Exception e) {
            throw new DatabaseException("Error Occurred while deleting user by id: " + id, e);
        }
        return false;
    }


    @Override
    public boolean checkIfUserSoftDeleted(String userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            return !user.get().isSoftDelete();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching the user {}", userId);
            String errorMessage = "Unexpected error occurred while fetching the user " + userId;
            throw new DatabaseException(errorMessage, e);
        }
    }
}