package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.entity.AutomatedMessage;
import edu.icet.ecom.repository.AutomatedMessageRepository;
import edu.icet.ecom.repository.impl.CustomerRepositoryImpl;
import edu.icet.ecom.util.HtmlEmailBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomatedMessageSchedulerService {

    private final AutomatedMessageRepository automatedMessageRepository;
    private final CustomerRepositoryImpl customerRepository;
    private final EmailService emailService;
    private final HtmlEmailBuilder htmlEmailBuilder;

    public void runScheduledAutomations() {
        try {

            log.info("AUTOMATED EMAIL SCHEDULER EXECUTION STARTED");
            log.info("Time: {} {}", LocalDate.now(), java.time.LocalTime.now());

            // Get all ACTIVE automated messages from DB
            List<AutomatedMessage> activeMessages = automatedMessageRepository.findActiveMessages();

            if (activeMessages.isEmpty()) {
                log.warn("No active automated messages found in DB");
            } else {
                log.info("Found {} active automated messages", activeMessages.size());

                // Process each active message (BIRTHDAY & ANNIVERSARY)
                for (AutomatedMessage message : activeMessages) {
                    try {
                        LocalDate tomorrow = LocalDate.now().plusDays(1);

                        if (message.getTriggerType() == AutomatedMessage.TriggerType.BIRTHDAY) {
                            sendBirthdayEmails(message, tomorrow);
                        } else if (message.getTriggerType() == AutomatedMessage.TriggerType.ANNIVERSARY) {
                            sendAnniversaryEmails(message, tomorrow);
                        }
                    } catch (Exception e) {
                        log.error("Error processing automated message ID {}: {}", message.getId(), e.getMessage(), e);
                    }
                }
            }

            log.info("AUTOMATED EMAIL SCHEDULER EXECUTION COMPLETED SUCCESSFULLY");

        } catch (Exception e) {
            log.error("ERROR IN AUTOMATED EMAIL SCHEDULER EXECUTION");
            log.error("Exception: {}", e.getMessage());
        }
    }

    /**
     * Send birthday emails based on active BIRTHDAY automated_messages
     */
    private void sendBirthdayEmails(AutomatedMessage message, LocalDate tomorrow) {
        log.info("BIRTHDAY EMAIL PROCESSING");
        log.info("Message ID: {}, Channel: {}, Active: {}", message.getId(), message.getChannel(), message.isActiveRule());

        // Skip if not EMAIL channel
        if (message.getChannel() != null && message.getChannel() != AutomatedMessage.Channel.EMAIL) {
            log.info("Skipping non-EMAIL channel: {}", message.getChannel());
            log.info("BIRTHDAY EMAIL PROCESSING END");
            return;
        }

        // Find customers with birthday tomorrow
        List<CustomerDto> birthdayCustomers = customerRepository.findCustomersWithBirthdayOn(tomorrow);
        log.info("│ Found {} customers with birthday tomorrow", birthdayCustomers.size());

        int successCount = 0;
        int failCount = 0;

        for (CustomerDto customer : birthdayCustomers) {
            try {
                // Check if customer opted in for email communication
                if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() != 1) {
                    log.debug("│ ⊘ Customer {} opted out of email", customer.getId());
                    continue;
                }

                // Build HTML email from automated_messages template_body
                String customerName = customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer";
                String subject = "🎉 Happy Birthday!";

                // Get template body and create HTML email
                String htmlBody = buildBirthdayHtmlEmail(customerName, message);

                // Send email
                emailService.sendEmailToCustomer(customer.getEmail(), subject, htmlBody);
                successCount++;
                log.info("│ ✓ Birthday email sent to customer {} ({})", customer.getId(), customer.getEmail());

            } catch (Exception e) {
                failCount++;
                log.error("│ ✗ Failed to send birthday email to customer {}: {}", customer.getId(), e.getMessage());
            }
        }

        log.info("Results: {} sent, {} failed", successCount, failCount);
        log.info("BIRTHDAY EMAIL PROCESSING END");
    }

    /**
     * Build HTML email for birthday using HtmlEmailBuilder
     */
    private String buildBirthdayHtmlEmail(String customerName, AutomatedMessage message) {
        String templateBody = message.getTemplateBody() != null ? message.getTemplateBody() : "Enjoy your special day!";

        // Replace placeholders
        templateBody = templateBody.replace("{firstName}", customerName)
                .replace("{discount}", message.getOfferValue() != null ? String.format("%.0f", message.getOfferValue()) : "0")
                .replace("{offerType}", message.getOfferType() != null ? message.getOfferType().toString() : "none");

        // Build email content
        StringBuilder content = new StringBuilder();
        content.append(htmlEmailBuilder.buildGreeting(customerName));
        content.append("<p>").append(templateBody).append("</p>");

        // Add offer section if applicable
        if (message.getOfferType() != null && message.getOfferType() != AutomatedMessage.OfferType.NONE) {
            String offerTitle = "🎁 Special Birthday Offer";
            String offerDesc = getOfferDescription(message);
            content.append(htmlEmailBuilder.buildPromoBanner(offerTitle, offerDesc, "Claim Offer", "https://restauranterp.com/offers"));
        }

        content.append(htmlEmailBuilder.buildFooter());

        return htmlEmailBuilder.wrapInHtml(content.toString(), "🎉 Happy Birthday!");
    }

    /**
     * Send anniversary emails based on active ANNIVERSARY automated_messages
     */
    private void sendAnniversaryEmails(AutomatedMessage message, LocalDate tomorrow) {
        log.info("ANNIVERSARY EMAIL PROCESSING");
        log.info("Message ID: {}, Channel: {}, Active: {}", message.getId(), message.getChannel(), message.isActiveRule());

        // Skip if not EMAIL channel
        if (message.getChannel() != null && message.getChannel() != AutomatedMessage.Channel.EMAIL) {
            log.info("Skipping non-EMAIL channel: {}", message.getChannel());
            log.info("ANNIVERSARY EMAIL PROCESSING END");
            return;
        }

        // Find customers with anniversary tomorrow
        List<CustomerDto> anniversaryCustomers = customerRepository.findCustomersWithAnniversaryOn(tomorrow);
        log.info("Found {} customers with anniversary tomorrow", anniversaryCustomers.size());

        int successCount = 0;
        int failCount = 0;

        for (CustomerDto customer : anniversaryCustomers) {
            try {
                // Check if customer opted in for email communication
                if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() != 1) {
                    log.debug("│ ⊘ Customer {} opted out of email", customer.getId());
                    continue;
                }

                // Build HTML email from automated_messages template_body
                String customerName = customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer";
                String subject = "🎊 Happy Anniversary!";

                // Get customer tenure
                int years = customer.getCreatedAt() != null
                        ? Math.max(java.time.Period.between(customer.getCreatedAt(), tomorrow).getYears(), 1)
                        : 1;

                // Create HTML email
                String htmlBody = buildAnniversaryHtmlEmail(customerName, years, message);

                // Send email
                emailService.sendEmailToCustomer(customer.getEmail(), subject, htmlBody);
                successCount++;
                log.info("✓ Anniversary email sent to customer {} ({})", customer.getId(), customer.getEmail());

            } catch (Exception e) {
                failCount++;
                log.error("✗ Failed to send anniversary email to customer {}: {}", customer.getId(), e.getMessage());
            }
        }

        log.info("Results: {} sent, {} failed", successCount, failCount);
        log.info("ANNIVERSARY EMAIL PROCESSING END");
    }

    /**
     * Build HTML email for anniversary using HtmlEmailBuilder - SAME STRUCTURE AS BIRTHDAY
     */
    private String buildAnniversaryHtmlEmail(String customerName, int years, AutomatedMessage message) {
        String templateBody = message.getTemplateBody() != null ? message.getTemplateBody() : "Thank you for your continued loyalty!";

        // Replace placeholders
        templateBody = templateBody.replace("{firstName}", customerName)
                .replace("{years}", String.valueOf(years))
                .replace("{discount}", message.getOfferValue() != null ? String.format("%.0f", message.getOfferValue()) : "0")
                .replace("{offerType}", message.getOfferType() != null ? message.getOfferType().toString() : "none");

        // Build email content - SAME STYLING AS BIRTHDAY EMAIL
        StringBuilder content = new StringBuilder();
        content.append(htmlEmailBuilder.buildGreeting(customerName));
        content.append("<p style=\"margin: 20px 0; line-height: 1.8; color: #555;\">It's been <strong>").append(years).append(" years</strong> since you joined us!</p>");
        content.append("<p style=\"margin: 20px 0; line-height: 1.8; color: #555;\">").append(templateBody).append("</p>");

        // Add offer section if applicable - SAME BANNER AS BIRTHDAY
        if (message.getOfferType() != null && message.getOfferType() != AutomatedMessage.OfferType.NONE) {
            String offerTitle = "🎊 Anniversary Celebration Offer";
            String offerDesc = getOfferDescription(message);
            content.append(htmlEmailBuilder.buildPromoBanner(offerTitle, offerDesc, "Claim Offer", "https://restauranterp.com/offers"));
        }

        content.append(htmlEmailBuilder.buildFooter());

        // USE SAME HTML WRAPPER AS BIRTHDAY - CONSISTENT LOOK AND FEEL
        return htmlEmailBuilder.wrapInHtml(content.toString(), "🎊 Happy Anniversary!");
    }

    /**
     * Get offer description based on offer type
     */
    private String getOfferDescription(AutomatedMessage message) {
        if (message.getOfferType() == AutomatedMessage.OfferType.DISCOUNT && message.getOfferValue() != null) {
            return "Enjoy " + message.getOfferValue().intValue() + "% OFF on your next visit!";
        } else if (message.getOfferType() == AutomatedMessage.OfferType.FREE_ITEM) {
            return "Get a FREE ITEM on your next visit!";
        }
        return "Special offer available for you!";
    }
}
