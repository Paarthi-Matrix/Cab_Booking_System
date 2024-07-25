package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.EmailRequestDto;
import com.i2i.zapcab.dto.RegisterCustomerRequestDto;
import org.springframework.stereotype.Component;

@Component
public interface EmailSenderService {
    void sendRegistrationEmailToDriver(EmailRequestDto emailRequestDto);
    void sendRegistrationMailtoCustomer(RegisterCustomerRequestDto registerRequestDto);
}
