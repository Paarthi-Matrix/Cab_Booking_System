package com.i2i.zapcab.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.UserRepository;

import java.util.Optional;

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
    public void saveUser(User user) {
        userRepository.save(user);
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
        userRepository.save(user);
        logger.info("Password changed successfully!");
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
