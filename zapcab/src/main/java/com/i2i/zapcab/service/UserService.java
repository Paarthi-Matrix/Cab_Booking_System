package com.i2i.zapcab.service;

import com.i2i.zapcab.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {
    User saveUser(User user);

    Optional<User> getUserById(String id);
    User getUserByMobileNumber(String mobileNumber);

    void changePassword(String id, String newPassword);
}
