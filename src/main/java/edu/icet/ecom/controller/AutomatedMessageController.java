package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.SendEmailRequest;
import edu.icet.ecom.entity.AutomatedMessage;
import edu.icet.ecom.repository.AutomatedMessageRepository;
import edu.icet.ecom.repository.impl.CustomerRepositoryImpl;
import edu.icet.ecom.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/automated-messages")
@RequiredArgsConstructor
@Slf4j
public class AutomatedMessageController {

    private final AutomatedMessageRepository automatedMessageRepository;
    private final EmailService emailService;
    private final CustomerRepositoryImpl customerRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<AutomatedMessage>> getAllActiveMessages() {
        log.info("Fetching all active automated messages");
        List<AutomatedMessage> messages = automatedMessageRepository.findActiveMessages();
        return ResponseEntity.ok(messages);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AutomatedMessage> getMessageById(@PathVariable Integer id) {
        log.info("Fetching automated message with ID: {}", id);
        Optional<AutomatedMessage> message = automatedMessageRepository.findById(id);
        return message.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/trigger/{triggerType}")
    public ResponseEntity<List<AutomatedMessage>> getMessagesByTriggerType(
            @PathVariable String triggerType) {
        log.info("Fetching automated messages for trigger type: {}", triggerType);
        try {
            AutomatedMessage.TriggerType type = AutomatedMessage.TriggerType.fromDbValue(triggerType);
            List<AutomatedMessage> messages = automatedMessageRepository.findActiveMessagesByTriggerType(type);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid trigger type: {}", triggerType);
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Integer> createMessage(@RequestBody AutomatedMessage message) {
        log.info("Creating new automated message: trigger={}, channel={}",
                message.getTriggerType(), message.getChannel());

        try {
            Integer id = automatedMessageRepository.save(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception e) {
            log.error("Error creating automated message", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMessage(@PathVariable Integer id,
                                               @RequestBody AutomatedMessage message) {
        log.info("Updating automated message with ID: {}", id);

        try {
            message.setId(id);
            boolean success = automatedMessageRepository.update(message);
            return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating automated message", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        log.info("Deleting automated message with ID: {}", id);

        boolean success = automatedMessageRepository.delete(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleMessageStatus(@PathVariable Integer id) {
        log.info("Toggling status of automated message with ID: {}", id);

        try {
            Optional<AutomatedMessage> messageOpt = automatedMessageRepository.findById(id);
            if (messageOpt.isPresent()) {
                AutomatedMessage message = messageOpt.get();
                message.setIsActive(!message.isActiveRule());
                boolean success = automatedMessageRepository.update(message);
                return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error toggling message status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/send-to-all")
    public ResponseEntity<Map<String, Object>> sendEmailToAllCustomers(@RequestBody SendEmailRequest request) {
        log.info("========== SEND EMAIL TO ALL CUSTOMERS ==========");
        log.info("Subject: {}", request.getSubject());

        try {
            // Get all customers directly from repository
            java.util.List<CustomerDto> customers = customerRepository.getAllCustomers();
            log.info("Total customers found in database: {}", customers.size());

            if (customers.isEmpty()) {
                log.warn("No customers found in database");
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "No customers found in database");
                response.put("sentCount", 0);
                return ResponseEntity.ok(response);
            }

            int sentCount = 0;
            int skippedCount = 0;

            for (CustomerDto customer : customers) {
                try {
                    String email = customer.getEmail();
                    Integer commEmail = customer.getCommunicationEmail();

                    log.debug("Processing customer: ID={}, Email={}, CommEmail={}",
                        customer.getId(), email, commEmail);

                    // Send only to customers with valid email and opted in
                    if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                        log.info("Sending email to: {}", email);
                        emailService.sendEmailToCustomer(email, request.getSubject(), request.getMessage());
                        sentCount++;
                        log.info("✓ Email sent to: {}", email);
                    } else {
                        skippedCount++;
                        String reason = "";
                        if (email == null || email.trim().isEmpty()) {
                            reason = "NO_EMAIL";
                        } else if (commEmail == null) {
                            reason = "COMM_EMAIL_IS_NULL";
                        } else if (commEmail != 1) {
                            reason = "OPTED_OUT (commEmail=" + commEmail + ")";
                        }
                        log.info("⊘ SKIPPED Customer {}: {} | Email={}, CommEmail={}", customer.getId(), reason, email, commEmail);
                    }
                } catch (Exception e) {
                    log.error("✗ Failed to send email to customer {}: {}", customer.getId(), e.getMessage());
                    skippedCount++;
                }
            }

            log.info("========== EMAIL SENDING COMPLETE ==========");
            log.info("Sent: {}, Skipped: {}, Total: {}", sentCount, skippedCount, customers.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Emails sent successfully");
            response.put("sentCount", sentCount);
            response.put("skippedCount", skippedCount);
            response.put("totalCustomers", customers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending emails to all customers: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error sending emails: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/send-birthday")
    public ResponseEntity<Map<String, Object>> sendBirthdayEmails(@RequestBody SendEmailRequest request) {
        log.info("========== SEND BIRTHDAY EMAILS ==========");
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.util.List<CustomerDto> customers = customerRepository.findCustomersWithBirthdayOn(today);
            log.info("Found {} customers with birthday today", customers.size());

            int sentCount = 0;
            int skippedCount = 0;

            for (CustomerDto customer : customers) {
                try {
                    String email = customer.getEmail();
                    Integer commEmail = customer.getCommunicationEmail();

                    if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                        log.info("Sending birthday email to: {}", email);
                        emailService.sendEmailToCustomer(email, request.getSubject(), request.getMessage());
                        sentCount++;
                        log.info("✓ Birthday email sent to: {}", email);
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    log.error("✗ Failed to send birthday email to customer {}: {}", customer.getId(), e.getMessage());
                    skippedCount++;
                }
            }

            log.info("Birthday email sending complete - Sent: {}, Skipped: {}", sentCount, skippedCount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Birthday emails sent successfully");
            response.put("sentCount", sentCount);
            response.put("skippedCount", skippedCount);
            response.put("totalMatched", customers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending birthday emails: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error sending emails: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/send-anniversary")
    public ResponseEntity<Map<String, Object>> sendAnniversaryEmails(@RequestBody SendEmailRequest request) {
        log.info("========== SEND ANNIVERSARY EMAILS ==========");
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.util.List<CustomerDto> customers = customerRepository.findCustomersWithAnniversaryOn(today);
            log.info("Found {} customers with anniversary today", customers.size());

            int sentCount = 0;
            int skippedCount = 0;

            for (CustomerDto customer : customers) {
                try {
                    String email = customer.getEmail();
                    Integer commEmail = customer.getCommunicationEmail();

                    if (email != null && !email.trim().isEmpty() && commEmail != null && commEmail == 1) {
                        log.info("Sending anniversary email to: {}", email);
                        emailService.sendEmailToCustomer(email, request.getSubject(), request.getMessage());
                        sentCount++;
                        log.info("✓ Anniversary email sent to: {}", email);
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    log.error("✗ Failed to send anniversary email to customer {}: {}", customer.getId(), e.getMessage());
                    skippedCount++;
                }
            }

            log.info("Anniversary email sending complete - Sent: {}, Skipped: {}", sentCount, skippedCount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Anniversary emails sent successfully");
            response.put("sentCount", sentCount);
            response.put("skippedCount", skippedCount);
            response.put("totalMatched", customers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending anniversary emails: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error sending emails: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/test-email")
    public ResponseEntity<Map<String, Object>> sendTestEmail(@RequestParam String toEmail) {
        log.info("Admin requesting to send test email to: {}", toEmail);
        try {
            String subject = "Test Email from Restaurant ERP";
            String message = "If you're reading this, the email system is working correctly! \n\n" +
                    "Test sent on: " + new java.util.Date() + "\n" +
                    "From: Restaurant ERP System";
            emailService.sendEmailToCustomer(toEmail, subject, message);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test email sent successfully to: " + toEmail);
            response.put("recipientEmail", toEmail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending test email to: {}", toEmail, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error sending test email: " + e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("recipientEmail", toEmail);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/debug/customer-status")
    public ResponseEntity<Map<String, Object>> debugCustomerStatus() {
        log.info("Checking customer email status");
        try {
            // Get all customers from repository
            java.util.List<CustomerDto> allCustomers = customerRepository.getAllCustomers();

            Map<String, Object> response = new HashMap<>();
            response.put("totalCustomers", allCustomers.size());

            // Count customers with valid emails
            long validEmailCount = allCustomers.stream()
                    .filter(c -> c.getEmail() != null && c.getEmail().trim().length() > 0)
                    .count();
            response.put("customersWithValidEmails", validEmailCount);

            // Count customers with communication_email = 1
            long optedInCount = allCustomers.stream()
                    .filter(c -> c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                    .count();
            response.put("customersOptedInForEmail", optedInCount);

            // Count eligible customers (valid email AND opted in AND not GDPR deleted)
            long eligibleCount = allCustomers.stream()
                    .filter(c -> c.getEmail() != null && c.getEmail().trim().length() > 0 &&
                               c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                    .count();
            response.put("eligibleCustomersForEmailing", eligibleCount);

            // Show first few customers
            java.util.List<Map<String, Object>> customerList = new java.util.ArrayList<>();
            for (CustomerDto customer : allCustomers) {
                Map<String, Object> custMap = new HashMap<>();
                custMap.put("id", customer.getId());
                custMap.put("name", customer.getFirstName() + " " + customer.getLastName());
                custMap.put("email", customer.getEmail());
                custMap.put("communicationEmail", customer.getCommunicationEmail());
                custMap.put("gdprDeleted", customer.getGdprDeleted());
                customerList.add(custMap);
            }
            response.put("customers", customerList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking customer status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

