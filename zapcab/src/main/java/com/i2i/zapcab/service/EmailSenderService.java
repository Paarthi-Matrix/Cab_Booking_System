package com.i2i.zapcab.service;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.dto.EmailInvoiceDto;
import com.i2i.zapcab.dto.EmailRequestDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;

/**
 * <p>
 * An interface used for sending registration email for both customer and driver.
 * </p>
 */
@Component
public interface EmailSenderService {

    /**
     * <p>
     * Sends a welcome email to a newly registered driver with their login credentials.
     * </p>
     *
     * @param emailRequestDto {@link EmailRequestDto}
     *                        Contains the email details such as recipient email address, driver name, mobile number, and password.
     */
    void sendRegistrationEmailToDriver(EmailRequestDto emailRequestDto);

    /**
     * <p>
     * Sends a welcome email to a newly registered customer with their login details.
     * </p>
     *
     * @param registerCustomerDto {@link RegisterCustomerDto}
     *                            Contains the customer registration details such as name, email, and phone number.
     */
    void sendRegistrationMailtoCustomer(RegisterCustomerDto registerCustomerDto);

    /**
     * <p>
     * Sends a rejection email to a newly registered driver with their login details.
     * </p>
     *
     * @param emailRequestDto {@link EmailRequestDto}
     *                        Contains the customer registration details such as name, email, and phone number.
     */
    void sendRejectionEmailToDriver(EmailRequestDto emailRequestDto);

    /**
     * <p>
     * This method is used to send the invoice email to the customer.
     * </p>
     *
     * @param emailInvoiceDto {@link EmailInvoiceDto}
     */
    void sendInvoiceMailtoCustomer(EmailInvoiceDto emailInvoiceDto);
}
