package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.UpdateDriverStatusDto;
import com.i2i.zapcab.model.Driver;
import org.springframework.stereotype.Component;

@Component
public interface DriverService {
    Driver saveDriver(Driver driver);
    void updateDriverStatusAndLocation(String id, UpdateDriverStatusDto updateDriverStatusDto);

    void changePassword(String id, String newPassword);
}
