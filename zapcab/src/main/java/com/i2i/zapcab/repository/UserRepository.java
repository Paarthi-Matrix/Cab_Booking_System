package com.i2i.zapcab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.i2i.zapcab.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByMobileNumber(String mobileNumber);
}
