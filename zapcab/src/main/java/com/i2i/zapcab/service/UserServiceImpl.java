package com.i2i.zapcab.service;

import com.i2i.zapcab.exception.UnexpectedException;
import com.i2i.zapcab.dto.MaskMobileNumberResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.UserRepository;

import java.util.Optional;

/**
 * <p>
 *     Implements {@link UserService}
 *     A service class that provides all the business logic for the following operations .
 *     <ol>
 *         <li> Changing password </li>
 *         <li> fetching user </li>
 *         <li> Masking the mobile number </li>
 *     </ol>
 * </p>
 */
@Service
public class UserServiceImpl implements UserService{

    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUserByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public void saveUsers(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Un expected error happened while saving/updating user " +
                    user.getName(), e);
            String errorMessage = "Un expected error happened while saving/updating user " +
                    user.getName();
            throw new UnexpectedException(errorMessage, e);
        }

    }

    /**
     * <p>
     *    This method is used to change the password for both the driver and customer
     * </p>
     * @param id
     * @param newPassword
     */
    @Override
    public void changePassword(String id, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        assert user != null;
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Un expected error happened while updating the new password for the user");
            String errorMessage = "Un expected error happened while updating the new password for the user";
            throw new UnexpectedException(errorMessage, e);
        }
        logger.info("Password changed successfully!");
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public MaskMobileNumberResponseDto updateMaskMobileNumber(String id, boolean mask) {
        User user = userRepository.findById(id).orElse(null);
        assert user!= null;
        user.setMaskedMobileNumber(mask);
        userRepository.save(user);
        return MaskMobileNumberResponseDto.builder().message("Updated successfully").build();
    }
}
