package com.i2i.zapcab.service;

import com.i2i.zapcab.dto.EmailRequestDto;
import com.i2i.zapcab.dto.RegisterCustomerDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.i2i.zapcab.common.ZapCabConstant.APP_DOMAIN_NAME;
import static com.i2i.zapcab.common.ZapCabConstant.EMAIL_SUBJECT_FOR_DRIVER;

/**
 * <p>
 *   Service class for sending emails in the ZapCab application.
 *   This service provides methods to send welcome emails to drivers and customers upon registration.
 * </p>
 */
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${from.email}")
    private static String fromEmail;

    /**
     * <p>
     * Sends a welcome email to a newly registered driver with their login credentials.
     * </p>
     * @param emailRequestDto {@link EmailRequestDto}
     *        Contains the email details such as recipient email address, driver name, mobile number, and password.
     */
    public void sendRegistrationEmailToDriver(EmailRequestDto emailRequestDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailRequestDto.getToEmail());
        message.setText(" Dear "+ emailRequestDto.getDriverName() + ",\n" +
                "\n" +
                "Welcome to ZapCab! We are excited to have you on board as a driver. Your account has been successfully created by our admin team. Below are your login details:\n" +
                "\n" +
                "Username(Your mobile number): " + emailRequestDto.getMobilNumber() +" \n" +
                "Password: "+ emailRequestDto.getPassword() + "\n" +
                "\n" +
                "Please use these credentials to log in to your account at "+ APP_DOMAIN_NAME +". For security reasons, we recommend changing your password after your first login.\n" +
                "\n" +
                "If you have any questions or need assistance, feel free to reach out to our support team at [Support Email/Phone Number].\n" +
                "\n" +
                "Best regards,\n" +
                "ZapCab Team\n" +
                "\n" +
                "Note: This is an automated message. Please do not reply to this email.\n");
        message.setSubject(EMAIL_SUBJECT_FOR_DRIVER);
        mailSender.send(message);
        logger.info("The welcome mail is sent to driver" + emailRequestDto.getDriverName() +
                " successfully!"
        );
    }

    /**
     * <p>
     * Sends a welcome email to a newly registered customer with their login details.
     * </p>
     * @param registerCustomerDto {@link RegisterCustomerDto}
     *       Contains the customer registration details such as name, email, and phone number.
     */
    public void sendRegistrationMailtoCustomer(RegisterCustomerDto registerCustomerDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(registerCustomerDto.getEmail());
        message.setText(" Dear "+ registerCustomerDto.getName() +",\n" +
                "\n" +
                "Welcome to ZapCab! We are thrilled to have you join our community. Your account has been successfully created, and you are now ready to book your rides with ease.\n" +
                "\n" +
                "Here are your login details:\n" +
                "Username(your mobile number): "+ registerCustomerDto.getMobileNumber() + " \n" +
                "\n" +
                "To get started, please log in to your account at " + APP_DOMAIN_NAME + " \n" +
                "\n" +
                "Getting Started with ZapCab:\n" +
                "1. Book a Ride: Simply enter your pickup and drop-off locations to book a ride.\n" +
                "2. Track Your Driver: Use our app to track your driver in real-time.\n" +
                "3. Payment Options: Choose from various payment options for a hassle-free experience.\n" +
                "4. Support: If you need any help, our support team is here for you at www.zapcabsupport.com.\n" +
                "\n" +
                "Tips for a Great Ride:\n" +
                "- Always verify your driver and vehicle details before starting the ride.\n" +
                "- Share your ride details with family or friends for added safety.\n" +
                "- Rate your driver after the trip to help us maintain a high-quality service.\n" +
                "\n" +
                "We are committed to providing you with a safe, reliable, and comfortable ride experience. If you have any questions or feedback, please don't hesitate to reach out to us.\n" +
                "\n" +
                "Welcome aboard, and we look forward to serving you!\n" +
                "\n" +
                "Best regards,\n" +
                "ZapCab Team\n" +
                "\n" +
                "Note: This is an automated message. Please do not reply to this email.");
        message.setSubject(EMAIL_SUBJECT_FOR_DRIVER);
        mailSender.send(message);
        logger.info("The welcome mail is sent to customer" + registerCustomerDto.getName() +
                " successfully!");
    }
}
