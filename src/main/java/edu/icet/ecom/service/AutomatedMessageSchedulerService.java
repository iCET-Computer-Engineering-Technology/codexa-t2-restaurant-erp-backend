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

@Service
@RequiredArgsConstructor
@Slf4j
public class AutomatedMessageSchedulerService {

    private final AutomatedMessageRepository automatedMessageRepository;
    private final CustomerRepositoryImpl customerRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

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

                // Compute the target event date
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

    @Scheduled(cron = "0 50 22 * * *")  // change this(what time to send birthday email- wauda)
    public void sendBirthdayEmailsAutomatically() {
        try {
            log.info("Starting automatic birthday email scheduler");

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

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

                    // Send birthday email
                    sendBirthdayEmail(customer);

                    log.info("Birthday email sent to customer {} ({})", customer.getId(), customer.getEmail());

                } catch (Exception e) {
                    log.error("Error sending birthday email to customer {} ({}): {}",
                            customer.getId(), customer.getEmail(), e.getMessage());
                    // Continue to next customer
                }
            }

            log.info("Automatic birthday email scheduler completed successfully");

        } catch (Exception e) {
            log.error("Error during automatic birthday email scheduling", e);
        }
    }

    /**
     * Send birthday email to a customer
     */
    private void sendBirthdayEmail(CustomerDto customer) {
        String subject = "🎂 Happy Birthday! Special Offer Inside";
        String body = String.format(
                "Dear %s,%n%n" +
                "Happy Birthday! 🎉%n%n" +
                "On your special day, we want to celebrate with you!%n%n" +
                "We have a special birthday discount waiting for you. " +
                "Enjoy 20%% off your next order at our restaurant.%n%n" +
                "Use this special birthday offer to treat yourself to your favorite meal.%n%n" +
                "Best wishes on your birthday!%n%n" +
                "Warmly,%n" +
                "The Restaurant Team",
                customer.getFirstName()
        );

        try {
            emailService.sendEmailToCustomer(customer.getEmail(), subject, body);
            log.debug("Birthday email sent successfully to {}", customer.getEmail());
        } catch (Exception e) {
            log.error("Failed to send birthday email to {}: {}", customer.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send birthday email", e);
        }
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
        // Check if customer has opted in and has valid email
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            log.debug("Customer {} has no email address", customer.getId());
            return;
        }

        if (customer.getCommunicationEmail() == null || customer.getCommunicationEmail() == 0) {
            log.debug("Customer {} has not opted in to email communications", customer.getId());
            return;
        }

        String subject = buildEmailSubject(eventType);
        String body = buildMessageBody(message, customer, eventType, eventDate);

        notificationService.sendEmail(customer.getEmail(), subject, body);
        log.info("Sent {} email to customer {} ({})", eventType, customer.getId(), customer.getEmail());
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

        // Use default template if none is configured
        if (template == null || template.trim().isEmpty()) {
            template = buildDefaultTemplate(message, eventType);
        }

        // Replace placeholders
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




