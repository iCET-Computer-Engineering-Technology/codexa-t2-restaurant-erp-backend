package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.SendEmailRequest;
import edu.icet.ecom.repository.impl.CustomerRepositoryImpl;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final NotificationService notificationService;
    private final CustomerRepositoryImpl customerRepository;

    @Override
    public int sendEmailToAllCustomers(SendEmailRequest request) {
        log.info("=========== STARTING EMAIL TO ALL CUSTOMERS ===========");
        log.info("Subject: {}", request.getSubject());

        List<CustomerDto> customers = customerRepository.getAllCustomers();
        log.info("Total customers in database: {}", customers.size());

        if (customers.isEmpty()) {
            log.warn("No registered customers found in database");
            return 0;
        }

        int sentCount = 0;
        int skippedCount = 0;

        for (CustomerDto customer : customers) {
            try {
                String email = customer.getEmail();
                Integer commEmail = customer.getCommunicationEmail();

                log.debug("Processing customer ID: {}, Email: {}, CommEmail: {}",
                    customer.getId(), email, commEmail);

                // Check if customer has opted in for email communication
                if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                    log.info("Sending email to: {} (Customer ID: {})", email, customer.getId());
                    notificationService.sendEmail(email, request.getSubject(), request.getMessage());
                    sentCount++;
                    log.info("✓ Email sent successfully to: {}", email);
                } else {
                    skippedCount++;
                    log.debug("⊘ Skipping customer ID: {} - Email: {}, CommEmail: {}",
                        customer.getId(), email, commEmail);
                }
            } catch (Exception e) {
                log.error("✗ Error sending email to customer {}: {}",
                    customer.getId(), e.getMessage(), e);
            }
        }

        log.info("=========== EMAIL SENDING COMPLETE ===========");
        log.info("Total sent: {}, Total skipped: {}, Total customers: {}",
            sentCount, skippedCount, customers.size());
        return sentCount;
    }

    @Override
    public void sendEmailToCustomer(String email, String subject, String body) {
        try {
            notificationService.sendEmail(email, subject, body);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public int sendEmailToBirthdayCustomers(SendEmailRequest request) {
        log.info("Sending birthday emails to customers with birthdays today");
        LocalDate today = LocalDate.now();
        List<CustomerDto> customers = customerRepository.findCustomersWithBirthdayOn(today);

        return sendEmailsToCustomers(customers, request);
    }

    @Override
    public int sendEmailToAnniversaryCustomers(SendEmailRequest request) {
        log.info("Sending anniversary emails to customers with anniversaries today");
        LocalDate today = LocalDate.now();
        List<CustomerDto> customers = customerRepository.findCustomersWithAnniversaryOn(today);

        return sendEmailsToCustomers(customers, request);
    }

    private int sendEmailsToCustomers(List<CustomerDto> customers, SendEmailRequest request) {
        if (customers.isEmpty()) {
            log.debug("No customers found for email campaign");
            return 0;
        }

        log.info("Processing {} customers for email campaign", customers.size());
        int sentCount = 0;
        int skippedCount = 0;

        for (CustomerDto customer : customers) {
            try {
                String email = customer.getEmail();
                Integer commEmail = customer.getCommunicationEmail();

                if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                    log.info("Sending email to: {} (Customer ID: {})", email, customer.getId());
                    sendEmailToCustomer(email, request.getSubject(), request.getMessage());
                    sentCount++;
                    log.info("✓ Email sent successfully to: {}", email);
                } else {
                    skippedCount++;
                    log.debug("⊘ Skipping customer ID: {} - Email: {}, CommEmail: {}",
                        customer.getId(), email, commEmail);
                }
            } catch (Exception e) {
                log.error("✗ Error sending email to customer {}: {}", customer.getId(), e.getMessage(), e);
            }
        }

        log.info("Email campaign complete - Sent: {}, Skipped: {}, Total: {}",
            sentCount, skippedCount, customers.size());
        return sentCount;
    }
}


