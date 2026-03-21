package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.SendEmailRequest;
import edu.icet.ecom.dto.MarketingEmailRequest;
import edu.icet.ecom.repository.impl.CustomerRepositoryImpl;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.EmailTemplateService;
import edu.icet.ecom.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final NotificationService notificationService;
    private final CustomerRepositoryImpl customerRepository;
    private final EmailTemplateService emailTemplateService;

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

                if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                    log.info("Sending email to: {} (Customer ID: {})", email, customer.getId());

                    String emailBody = buildTemplatedEmail(customer, request);

                    notificationService.sendEmail(email, request.getSubject(), emailBody);
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

        if (customers.isEmpty()) {
            log.info("No customers with birthdays today");
            return 0;
        }

        int sentCount = 0;
        for (CustomerDto customer : customers) {
            try {
                String email = customer.getEmail();
                Integer commEmail = customer.getCommunicationEmail();

                if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                    log.info("Sending birthday email to: {} (Customer ID: {})", email, customer.getId());

                    String emailBody = buildBirthdayEmailTemplate(customer);

                    sendEmailToCustomer(email, "🎉 Happy Birthday, " + customer.getFirstName() + "!", emailBody);
                    sentCount++;
                    log.info("✓ Birthday email sent successfully to: {}", email);
                } else {
                    log.debug("⊘ Skipping birthday email for customer ID: {} - no email consent", customer.getId());
                }
            } catch (Exception e) {
                log.error("✗ Error sending birthday email to customer {}: {}", customer.getId(), e.getMessage(), e);
            }
        }

        log.info("Birthday email campaign complete - Sent: {}, Total: {}", sentCount, customers.size());
        return sentCount;
    }

    private String buildBirthdayEmailTemplate(CustomerDto customer) {
        String template = emailTemplateService.getBirthdayEmailTemplate();

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("discount", "25%");
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", LocalDate.now().plusDays(7).toString());

        return emailTemplateService.renderTemplate(template, variables);
    }

    @Override
    public int sendEmailToAnniversaryCustomers(SendEmailRequest request) {
        log.info("Sending anniversary emails to customers with anniversaries today");
        LocalDate today = LocalDate.now();
        List<CustomerDto> customers = customerRepository.findCustomersWithAnniversaryOn(today);

        if (customers.isEmpty()) {
            log.info("No customers with anniversaries today");
            return 0;
        }

        int sentCount = 0;
        for (CustomerDto customer : customers) {
            try {
                String email = customer.getEmail();
                Integer commEmail = customer.getCommunicationEmail();

                if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                    log.info("Sending anniversary email to: {} (Customer ID: {})", email, customer.getId());

                    // Build beautiful anniversary email with HTML template
                    String emailBody = buildAnniversaryEmailTemplate(customer);

                    sendEmailToCustomer(email, "🎊 Happy Anniversary with Restaurant ERP!", emailBody);
                    sentCount++;
                    log.info("✓ Anniversary email sent successfully to: {}", email);
                } else {
                    log.debug("⊘ Skipping anniversary email for customer ID: {} - no email consent", customer.getId());
                }
            } catch (Exception e) {
                log.error("✗ Error sending anniversary email to customer {}: {}", customer.getId(), e.getMessage(), e);
            }
        }

        log.info("Anniversary email campaign complete - Sent: {}, Total: {}", sentCount, customers.size());
        return sentCount;
    }

    private String buildAnniversaryEmailTemplate(CustomerDto customer) {
        String template = emailTemplateService.getAnniversaryEmailTemplate();

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("discount", "20%");
        variables.put("years", "5");
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", LocalDate.now().plusDays(7).toString());

        return emailTemplateService.renderTemplate(template, variables);
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

                    // Build HTML email with template if available
                    String emailBody = buildTemplatedEmail(customer, request);

                    sendEmailToCustomer(email, request.getSubject(), emailBody);
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

    private String buildTemplatedEmail(CustomerDto customer, SendEmailRequest request) {

        String template = emailTemplateService.getPromotionalEmailTemplate();

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("discount", extractOfferFromMessage(request.getMessage()));
        variables.put("restaurant", "Restaurant ERP");

        return emailTemplateService.renderTemplate(template, variables);
    }

    private String extractOfferFromMessage(String message) {
        if (message != null && message.contains("%")) {
            int percentIndex = message.indexOf("%");
            if (percentIndex > 0) {
                int startIndex = Math.max(0, percentIndex - 3);
                String offer = message.substring(startIndex, percentIndex + 1);
                return offer.replaceAll("[^0-9%]", "");
            }
        }
        return "Special Offer";
    }

    @Override
    public boolean sendMarketingEmail(MarketingEmailRequest request) {
        log.info("Sending marketing email to: {} | Template: {}",
            request.getRecipientEmail(), request.getTemplateType());

        try {
            String template = getTemplate(request.getTemplateType());

            String htmlBody = emailTemplateService.renderTemplate(
                template,
                request.getTemplateVariables()
            );

            notificationService.sendEmail(
                request.getRecipientEmail(),
                request.getSubject(),
                htmlBody
            );

            log.info("✓ Marketing email sent successfully to: {}", request.getRecipientEmail());
            return true;
        } catch (Exception e) {
            log.error("✗ Failed to send marketing email to {}: {}",
                request.getRecipientEmail(), e.getMessage(), e);
            return false;
        }
    }

    private String getTemplate(String templateType) {
        return switch (templateType.toLowerCase()) {
            case "birthday" -> emailTemplateService.getBirthdayEmailTemplate();
            case "anniversary" -> emailTemplateService.getAnniversaryEmailTemplate();
            case "campaign" -> emailTemplateService.getMarketingCampaignTemplate();
            default -> emailTemplateService.getPromotionalEmailTemplate();
        };
    }
}
