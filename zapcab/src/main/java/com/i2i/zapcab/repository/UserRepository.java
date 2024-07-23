package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByMobileNumber(String phoneNumber);
}
