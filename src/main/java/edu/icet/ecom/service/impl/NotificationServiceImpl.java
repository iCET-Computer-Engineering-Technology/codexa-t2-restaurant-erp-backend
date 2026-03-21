package edu.icet.ecom.service.impl;

import edu.icet.ecom.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private static final String FROM_EMAIL = "wasudadark999@gmail.com";
    private static final String BUSINESS_NAME = "Restaurant_ERP_BETA";

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            // Create mime message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set email details
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true for HTML content
            helper.setFrom(FROM_EMAIL, BUSINESS_NAME);

            // Send the email
            mailSender.send(mimeMessage);
            log.info("Email sent successfully to: {} | Subject: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {} | Subject: {} | Error: {}", to, subject, e.getMessage(), e);
            throw new RuntimeException("Failed to send email to " + to, e);
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {} | Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Unexpected error sending email", e);
        }
    }

    @Override
    public void sendSms(String to, String body) {
        // SMS implementation - placeholder for future integration
        log.warn("SMS sending not yet implemented. Message for {}: {}", to, body);
        // TODO: Implement SMS sending by provider
    }
}



