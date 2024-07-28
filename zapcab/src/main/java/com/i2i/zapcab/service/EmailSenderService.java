package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.EmailInvoiceDto;
import com.i2i.zapcab.dto.EmailRequestDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     An interface to perform email service operation
 *     The operations includes sending email to the registered customer and the registered driver
 * </p>
 */
@Component
public interface EmailSenderService {
    void sendRegistrationEmailToDriver(EmailRequestDto emailRequestDto);
    void sendInvoiceMailtoCustomer(EmailInvoiceDto emailInvoiceDto);
    void sendRegistrationMailtoCustomer(RegisterCustomerDto registerCustomerDto);
}
