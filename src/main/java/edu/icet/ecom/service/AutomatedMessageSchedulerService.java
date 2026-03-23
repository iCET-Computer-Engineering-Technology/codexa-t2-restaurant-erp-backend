package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.entity.AutomatedMessage;
import edu.icet.ecom.repository.AutomatedMessageRepository;
import edu.icet.ecom.repository.impl.CustomerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomatedMessageSchedulerService {

    private final AutomatedMessageRepository automatedMessageRepository;
    private final CustomerRepositoryImpl customerRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    @Scheduled(cron = "${app.notifications.cron:0 0 8 * * *}")
    public void processAutomatedMessages() {
        try {
            log.info("Starting automated message scheduling job");

            LocalDate today = LocalDate.now();
            List<AutomatedMessage> activeMessages = automatedMessageRepository.findActiveMessages();

            if (activeMessages.isEmpty()) {
                log.warn("No active automated messages configured");
                return;
            }

            log.debug("Found {} active automated message configurations", activeMessages.size());

            for (AutomatedMessage message : activeMessages) {
                if (!message.isActiveRule()) {
                    continue;
                }

                LocalDate targetDate = today.plusDays(message.getSendDaysBeforeWithDefault());

                switch (message.getTriggerType()) {
                    case BIRTHDAY -> handleBirthdayMessages(message, targetDate);
                    case ANNIVERSARY -> handleAnniversaryMessages(message, targetDate);
                    default -> log.debug("Trigger type {} not yet implemented", message.getTriggerType());
                }
            }

            log.info("Automated message scheduling job completed successfully");
        } catch (Exception e) {
            log.error("Error during automated message processing", e);
        }
    }

    @Scheduled(cron = "0 47 11 * * *")
    public void sendBirthdayEmailsAutomatically() {
        try {
            log.info("Starting automatic birthday email scheduler");

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            Optional<AutomatedMessage> birthdayMessage = automatedMessageRepository
                    .findActiveMessagesByTriggerType(AutomatedMessage.TriggerType.BIRTHDAY)
                    .stream()
                    .filter(msg -> msg.getChannel() == null || msg.getChannel() == AutomatedMessage.Channel.EMAIL)
                    .findFirst();

            if (birthdayMessage.isEmpty()) {
                log.debug("No active EMAIL automated message for birthday; skipping birthday scheduler run");
                return;
            }

            List<CustomerDto> birthdayCustomers = customerRepository.findCustomersWithBirthdayOn(tomorrow);

            if (birthdayCustomers.isEmpty()) {
                log.debug("No customers have birthday tomorrow ({})", tomorrow);
                return;
            }

            log.info("Found {} customers with birthday tomorrow ({})", birthdayCustomers.size(), tomorrow);

            for (CustomerDto customer : birthdayCustomers) {
                try {
                    if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() != 1) {
                        log.debug("Customer {} has email communication disabled, skipping birthday email", customer.getId());
                        continue;
                    }

                    // Send HTML birthday email using automated message if available
                    sendBirthdayEmailHtml(customer, tomorrow, birthdayMessage.orElse(null));

                    log.info("Birthday email sent to customer {} ({})", customer.getId(), customer.getEmail());

                } catch (Exception e) {
                    log.error("Error sending birthday email to customer {} ({}): {}",
                            customer.getId(), customer.getEmail(), e.getMessage());
                }
            }

            log.info("Automatic birthday email scheduler completed successfully");

        } catch (Exception e) {
            log.error("Error during automatic birthday email scheduling", e);
        }
    }

    private void sendBirthdayEmailHtml(CustomerDto customer, LocalDate birthdayDate, AutomatedMessage autoMessage) {

        String template = emailTemplateService.getBirthdayEmailTemplate();

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");

        String discountValue = autoMessage != null && autoMessage.hasDiscount()
                ? String.format("%.0f", autoMessage.getOfferValue())
                : "25";
        variables.put("discount", discountValue);
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", birthdayDate.plusDays(7).toString());

        if (autoMessage != null && autoMessage.getTemplateBody() != null && !autoMessage.getTemplateBody().trim().isEmpty()) {
            String customMessage = buildMessageBody(autoMessage, customer, "birthday", birthdayDate);
            variables.put("offerMessage", customMessage);

            template = template.replace("Enjoy our exclusive {{discount}}% birthday discount on your next visit!", "{{offerMessage}}");
        }

        String htmlBody = emailTemplateService.renderTemplate(template, variables);

        String subject = "🎉 Happy Birthday, " + (customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer") + "!";
        emailService.sendEmailToCustomer(customer.getEmail(), subject, htmlBody);

        log.debug("Beautiful birthday email sent successfully to {}", customer.getEmail());
    }

    @Scheduled(cron = "0 09 22 * * *")
    public void sendAnniversaryEmailsAutomatically() {
        try {
            log.info("Starting automatic anniversary email scheduler");

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            Optional<AutomatedMessage> anniversaryMessage = automatedMessageRepository
                    .findActiveMessagesByTriggerType(AutomatedMessage.TriggerType.ANNIVERSARY)
                    .stream()
                    .filter(msg -> msg.getChannel() == null || msg.getChannel() == AutomatedMessage.Channel.EMAIL)
                    .findFirst();

            if (anniversaryMessage.isEmpty()) {
                log.debug("No active EMAIL automated message for anniversary; skipping anniversary scheduler run");
                return;
            }

            List<CustomerDto> anniversaryCustomers = customerRepository.findCustomersWithAnniversaryOn(tomorrow);

            if (anniversaryCustomers.isEmpty()) {
                log.debug("No customers have anniversary tomorrow ({})", tomorrow);
                return;
            }

            log.info("Found {} customers with anniversary tomorrow ({})", anniversaryCustomers.size(), tomorrow);

            for (CustomerDto customer : anniversaryCustomers) {
                try {
                    if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() != 1) {
                        log.debug("Customer {} has email communication disabled, skipping anniversary email", customer.getId());
                        continue;
                    }
                    if (customer.getCreatedAt() == null) {
                        log.debug("Customer {} missing created_at, skipping anniversary email", customer.getId());
                        continue;
                    }

                    // Send HTML anniversary email using automated message if available
                    sendAnniversaryEmailHtml(customer, tomorrow, anniversaryMessage.orElse(null));

                    log.info("Anniversary email sent to customer {} ({})", customer.getId(), customer.getEmail());

                } catch (Exception e) {
                    log.error("Error sending anniversary email to customer {} ({}): {}",
                            customer.getId(), customer.getEmail(), e.getMessage());
                }
            }

            log.info("Automatic anniversary email scheduler completed successfully");

        } catch (Exception e) {
            log.error("Error during automatic anniversary email scheduling", e);
        }
    }

    private void sendAnniversaryEmailHtml(CustomerDto customer, LocalDate anniversaryDate, AutomatedMessage autoMessage) {

        String template = emailTemplateService.getAnniversaryEmailTemplate();

        int yearsWithUs = Math.max(java.time.Period.between(customer.getCreatedAt(), anniversaryDate).getYears(), 1);

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("years", String.valueOf(yearsWithUs));

        String discountValue = autoMessage != null && autoMessage.hasDiscount()
                ? String.format("%.0f", autoMessage.getOfferValue())
                : "20";
        variables.put("discount", discountValue);
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", anniversaryDate.plusDays(7).toString());

        if (autoMessage != null && autoMessage.getTemplateBody() != null && !autoMessage.getTemplateBody().trim().isEmpty()) {
            String customMessage = buildMessageBody(autoMessage, customer, "anniversary", anniversaryDate);
            variables.put("offerMessage", customMessage);
            // Replace the default offer message placeholder with our custom message
            template = template.replace("Thank you for {{years}} wonderful years! Here's {{discount}}% off for our valued customer.", "{{offerMessage}}");
        }

        String htmlBody = emailTemplateService.renderTemplate(template, variables);
        String subject = "🎉 Happy Anniversary with Restaurant ERP!";
        emailService.sendEmailToCustomer(customer.getEmail(), subject, htmlBody);

        log.debug("Beautiful anniversary email sent successfully to {}", customer.getEmail());
    }

    private void handleBirthdayMessages(AutomatedMessage message, LocalDate targetDate) {
        log.debug("Processing birthday messages for target date: {}", targetDate);

        List<CustomerDto> customers = customerRepository.findCustomersWithBirthdayOn(targetDate);

        if (customers.isEmpty()) {
            log.debug("No customers with birthday on {}", targetDate);
            return;
        }

        log.info("Found {} customers with birthday on {}", customers.size(), targetDate);

        for (CustomerDto customer : customers) {
            sendMessageToCustomer(message, customer, "birthday", targetDate);
        }
    }

    private void handleAnniversaryMessages(AutomatedMessage message, LocalDate targetDate) {
        log.debug("Processing anniversary messages for target date: {}", targetDate);

        List<CustomerDto> customers = customerRepository.findCustomersWithAnniversaryOn(targetDate);

        if (customers.isEmpty()) {
            log.debug("No customers with anniversary on {}", targetDate);
            return;
        }

        log.info("Found {} customers with anniversary on {}", customers.size(), targetDate);

        for (CustomerDto customer : customers) {
            sendMessageToCustomer(message, customer, "anniversary", targetDate);
        }
    }

    private void sendMessageToCustomer(AutomatedMessage message, CustomerDto customer,
                                       String eventType, LocalDate eventDate) {
        try {
            // All messages are sent via email
            sendEmailToCustomer(message, customer, eventType, eventDate);
        } catch (Exception e) {
            log.error("Error sending {} message to customer ID {}: {}",
                    eventType, customer.getId(), e.getMessage());
        }
    }

    private void sendEmailToCustomer(AutomatedMessage message, CustomerDto customer,
                                      String eventType, LocalDate eventDate) {

        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            log.debug("Customer {} has no email address", customer.getId());
            return;
        }

        if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() == 0) {
            log.debug("Customer {} has not opted in to email communications", customer.getId());
            return;
        }

        String subject = buildEmailSubject(eventType);
        String htmlBody = buildHtmlEmailBody(message, customer, eventType, eventDate);

        emailService.sendEmailToCustomer(customer.getEmail(), subject, htmlBody);
        log.info("Sent {} email to customer {} ({})", eventType, customer.getId(), customer.getEmail());
    }

    private String buildHtmlEmailBody(AutomatedMessage message, CustomerDto customer,
                                       String eventType, LocalDate eventDate) {
        String template;
        int yearsWithUs = 1;

        if ("birthday".equalsIgnoreCase(eventType)) {
            template = emailTemplateService.getBirthdayEmailTemplate();
        } else if ("anniversary".equalsIgnoreCase(eventType)) {
            template = emailTemplateService.getAnniversaryEmailTemplate();
            if (customer.getCreatedAt() != null) {
                yearsWithUs = Math.max(java.time.Period.between(customer.getCreatedAt(), eventDate).getYears(), 1);
            }
        } else {
            template = emailTemplateService.getPromotionalEmailTemplate();
        }

        if (message != null && message.getTemplateBody() != null && !message.getTemplateBody().trim().isEmpty()) {
            String customMessage = buildMessageBody(message, customer, eventType, eventDate);
            if ("birthday".equalsIgnoreCase(eventType)) {
                template = template.replace("Enjoy our exclusive {{discount}}% birthday discount on your next visit!", customMessage);
            } else if ("anniversary".equalsIgnoreCase(eventType)) {
                template = template.replace("Thank you for {{years}} wonderful years! Here's {{discount}}% off for our valued customer.", customMessage);
            } else {
                template = template.replace("{{discount}}", customMessage);
            }
        }

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("discount", message != null && message.hasDiscount()
                ? String.format("%.0f", message.getOfferValue())
                : ("birthday".equalsIgnoreCase(eventType) ? "25" : "20"));
        variables.put("restaurant", "Restaurant ERP");
        if ("anniversary".equalsIgnoreCase(eventType)) {
            variables.put("years", String.valueOf(yearsWithUs));
        }
        variables.put("expiryDate", eventDate.plusDays(7).toString());

        return emailTemplateService.renderTemplate(template, variables);
    }

    private String buildEmailSubject(String eventType) {
        return switch (eventType.toLowerCase()) {
            case "birthday" -> "🎂 Happy Birthday from Our Restaurant!";
            case "anniversary" -> "🎉 Happy Anniversary with Us!";
            default -> "A Special Offer Just for You";
        };
    }

    private String buildMessageBody(AutomatedMessage message, CustomerDto customer,
                                    String eventType, LocalDate eventDate) {
        String template = message.getTemplateBody();

        if (template == null || template.trim().isEmpty()) {
            template = buildDefaultTemplate(message, eventType);
        }

        return template
                .replace("{firstName}", sanitize(customer.getFirstName(), "Valued Customer"))
                .replace("{lastName}", sanitize(customer.getLastName(), ""))
                .replace("{eventType}", eventType)
                .replace("{eventDate}", eventDate.toString())
                .replace("{offerType}", message.getOfferType() != null
                        ? message.getOfferType().toString().toLowerCase()
                        : "none")
                .replace("{offerValue}", message.getOfferValue() != null
                        ? String.format("%.0f", message.getOfferValue())
                        : "0");
    }

    private String buildDefaultTemplate(AutomatedMessage message, String eventType) {
        String greeting = String.format("Dear {firstName},%n%nHappy %s! ", eventType);

        return switch (message.getOfferType()) {
            case DISCOUNT -> greeting + "Enjoy a {offerValue}% discount on your next visit. " +
                    "Use this offer before {eventDate}.";
            case FREE_ITEM -> greeting + "Enjoy a free item on your next visit. " +
                    "Valid on or around {eventDate}.";
            default -> greeting + "We appreciate your loyalty and wish you a wonderful day!";
        };
    }

    private String sanitize(String value, String defaultValue) {
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }
}
