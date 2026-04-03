package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.MarketingEmailRequest;
import edu.icet.ecom.dto.EmailSchedulerConfigDto;
import edu.icet.ecom.entity.AutomatedMessage;
import edu.icet.ecom.service.EmailTemplateService;
import edu.icet.ecom.repository.AutomatedMessageRepository;
import edu.icet.ecom.repository.CustomerRepository;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.EmailSchedulerConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/automated-messages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Automated Messages", description = "Automated message configuration and email operations")
public class AutomatedMessageController {

    private final AutomatedMessageRepository automatedMessageRepository;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;
    private final EmailTemplateService emailTemplateService;
    private final EmailSchedulerConfigService emailSchedulerConfigService;

    //automated message operations
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<AutomatedMessage>> getAllActiveMessages() {
        log.info("Fetching all active automated messages");
        List<AutomatedMessage> messages = automatedMessageRepository.findActiveMessages();
        return ResponseEntity.ok(messages);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/inactive")
    public ResponseEntity<List<AutomatedMessage>> getAllInactiveMessages() {
        log.info("Fetching all inactive automated messages");
        List<AutomatedMessage> messages = automatedMessageRepository.findInactiveMessages();
        return ResponseEntity.ok(messages);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AutomatedMessage> getMessageById(@PathVariable Integer id) {
        log.info("Fetching automated message with ID: {}", id);
        Optional<AutomatedMessage> message = automatedMessageRepository.findById(id);
        return message.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        log.info("Deleting automated message with ID: {}", id);

        boolean success = automatedMessageRepository.delete(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    //Email operations
    @PostMapping("/send-marketing")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send marketing email with template",
               description = "Send a marketing email with dynamic template placeholders")
    public ResponseEntity<?> sendMarketingEmail(@RequestBody MarketingEmailRequest request) {
        try {
            if (request.getRecipientEmail() == null || request.getRecipientEmail().trim().isEmpty()) {
                return new ResponseEntity<>(
                    "Recipient email is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            if (request.getTemplateType() == null || request.getTemplateType().trim().isEmpty()) {
                request.setTemplateType("promotional");
            }

            if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
                request.setSubject("Special Offer from Restaurant ERP");
            }

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                return new ResponseEntity<>(
                    "Marketing email sent successfully to: " + request.getRecipientEmail(),
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    "Failed to send marketing email",
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Error sending marketing email", e);
            return new ResponseEntity<>(
                "Error sending email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-birthday")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send birthday email (unified)",
               description = "Send a birthday email - accepts query params or JSON body or both")
    public ResponseEntity<?> sendBirthdayEmailUnified(
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(required = false) String recipientName,
            @RequestParam(required = false) String discount,
            @RequestBody(required = false) MarketingEmailRequest bodyRequest) {
        try {
            log.info("=== Birthday Email Request ===");
            log.info("Query Params - Email: {}, Name: {}, Discount: {}", recipientEmail, recipientName, discount);
            log.info("Request Body: {}", bodyRequest);

            if (bodyRequest != null) {
                if ((recipientEmail == null || recipientEmail.trim().isEmpty()) && bodyRequest.getRecipientEmail() != null) {
                    recipientEmail = bodyRequest.getRecipientEmail();
                    log.info("Using recipientEmail from JSON body: {}", recipientEmail);
                }
                if ((recipientName == null || recipientName.trim().isEmpty()) && bodyRequest.getRecipientName() != null) {
                    recipientName = bodyRequest.getRecipientName();
                    log.info("Using recipientName from JSON body: {}", recipientName);
                }
                if ((discount == null || discount.trim().isEmpty()) && bodyRequest.getTemplateVariables() != null
                        && bodyRequest.getTemplateVariables().containsKey("discount")) {
                    discount = bodyRequest.getTemplateVariables().get("discount");
                    log.info("Using discount from JSON body: {}", discount);
                }
            }

            if (discount == null || discount.trim().isEmpty()) {
                discount = "25";
            }

            log.info("Final parameters - Email: {}, Name: {}, Discount: {}", recipientEmail, recipientName, discount);

            boolean singleRecipient = recipientEmail != null && !recipientEmail.trim().isEmpty();

            Optional<AutomatedMessage> activeMsg = automatedMessageRepository
                    .findActiveMessagesByTriggerType(AutomatedMessage.TriggerType.BIRTHDAY)
                    .stream()
                    .filter(msg -> msg.getChannel() == null || msg.getChannel() == AutomatedMessage.Channel.EMAIL)
                    .findFirst();

            if (activeMsg.isEmpty()) {
                log.warn("No active automated message found for birthday trigger");
                return new ResponseEntity<>(
                    "No active birthday automated message configured",
                    HttpStatus.BAD_REQUEST
                );
            }

            if (singleRecipient) {
                if (recipientName == null || recipientName.trim().isEmpty()) {
                    recipientName = "Valued Customer";
                }

                try {
                    String html = buildBirthdayHtml(recipientName, discount, activeMsg.get());
                    String subject = "🎉 Happy Birthday, " + recipientName + "!";
                    emailService.sendEmailToCustomer(recipientEmail.trim(), subject, html);
                    log.info("✓ Birthday email sent successfully to: {}", recipientEmail);
                    return new ResponseEntity<>(
                        "✓ Birthday email sent successfully to: " + recipientEmail,
                        HttpStatus.OK
                    );
                } catch (Exception ex) {
                    log.error("✗ Failed to send birthday email to {}: {}", recipientEmail, ex.getMessage());
                    return new ResponseEntity<>(
                        "✗ Failed to send birthday email to: " + recipientEmail,
                        HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }
            }

            //broadcast to all customers when no recipientEmail is provided
            List<CustomerDto> customers = customerRepository.getAllCustomers();
            long successCount = 0;
            long failCount = 0;


            for (CustomerDto customer : customers) {
                if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                    continue; //skip customers without email
                }
                Integer commEmail = customer.getCommunicationEmail();
                if (commEmail != null && commEmail == 0) {
                    continue; //respect communication preference
                }

                String name = (customer.getFirstName() != null && !customer.getFirstName().isEmpty())
                        ? customer.getFirstName()
                        : "Valued Customer";

                try {
                    String html = buildBirthdayHtml(name, discount, activeMsg.get());
                    String subject = "🎉 Happy Birthday, " + name + "!";
                    emailService.sendEmailToCustomer(customer.getEmail().trim(), subject, html);
                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    log.warn("Failed to send birthday email to {}: {}", customer.getEmail(), ex.getMessage());
                }
            }

            if (successCount == 0) {
                log.warn("Birthday broadcast: no emails were sent ({} skipped/fail)", failCount);
                return new ResponseEntity<>(
                    "No customers with valid email preferences to send birthday email",
                    HttpStatus.BAD_REQUEST
                );
            }

            log.info("Birthday broadcast completed - success: {}, failed: {}", successCount, failCount);
            return new ResponseEntity<>(
                "Birthday emails sent. Success: " + successCount + ", Failed: " + failCount,
                HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("Exception in sendBirthdayEmailUnified: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending birthday email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-birthday-request")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send birthday email (request body)",
               description = "Send a birthday email with special discount offer using JSON request body")
    public ResponseEntity<?> sendBirthdayEmailRequest(@RequestBody MarketingEmailRequest request) {
        try {
            if (request == null || request.getRecipientEmail() == null || request.getRecipientEmail().trim().isEmpty()) {
                log.warn("Birthday email request rejected: recipientEmail is missing");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            String recipientName = request.getRecipientName() != null && !request.getRecipientName().trim().isEmpty()
                ? request.getRecipientName()
                : "Valued Customer";

            String discount = "25";
            if (request.getTemplateVariables() != null && request.getTemplateVariables().containsKey("discount")) {
                discount = request.getTemplateVariables().get("discount");
            }

            log.info("Send birthday email - Email: {}, Name: {}, Discount: {}", request.getRecipientEmail(), recipientName, discount);

            MarketingEmailRequest emailRequest = MarketingEmailRequest.builder()
                .recipientEmail(request.getRecipientEmail().trim())
                .recipientName(recipientName.trim())
                .templateType("birthday")
                .subject("Happy Birthday, " + recipientName + "! 🎉")
                .build();

            emailRequest.addVariable("discount", discount + "%");
            emailRequest.addVariable("restaurant", "Restaurant ERP");
            emailRequest.addVariable("expiryDate", LocalDate.now().plusDays(7).toString());

            boolean sent = emailService.sendMarketingEmail(emailRequest);

            if (sent) {
                log.info("✓ Birthday email sent successfully to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✓ Birthday email sent successfully to: " + request.getRecipientEmail(),
                    HttpStatus.OK
                );
            } else {
                log.error("✗ Failed to send birthday email to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✗ Failed to send birthday email to: " + request.getRecipientEmail(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendBirthdayEmailRequest: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending birthday email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-anniversary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send anniversary email",
               description = "Send an anniversary email with special discount offer")
    public ResponseEntity<?> sendAnniversaryEmail(
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(required = false) String recipientName,
            @RequestParam(required = false) String discount,
            @RequestParam(required = false) String years) {
        try {
            if (discount == null || discount.trim().isEmpty()) {
                discount = "20";
            }

            boolean singleRecipient = recipientEmail != null && !recipientEmail.trim().isEmpty();

            Optional<AutomatedMessage> activeMsg = automatedMessageRepository
                    .findActiveMessagesByTriggerType(AutomatedMessage.TriggerType.ANNIVERSARY)
                    .stream()
                    .filter(msg -> msg.getChannel() == null || msg.getChannel() == AutomatedMessage.Channel.EMAIL)
                    .findFirst();

            if (activeMsg.isEmpty()) {
                log.warn("No active automated message found for anniversary trigger");
                return new ResponseEntity<>(
                    "No active anniversary automated message configured",
                    HttpStatus.BAD_REQUEST
                );
            }

            if (singleRecipient) {
                if (recipientName == null || recipientName.trim().isEmpty()) {
                    recipientName = "Valued Customer";
                }
                if (years == null || years.trim().isEmpty()) {
                    years = "5";
                }

                log.info("Send anniversary email - Email: {}, Name: {}, Discount: {}, Years: {}", recipientEmail, recipientName, discount, years);

                MarketingEmailRequest request = MarketingEmailRequest.builder()
                    .recipientEmail(recipientEmail.trim())
                    .recipientName(recipientName.trim())
                    .templateType("anniversary")
                    .subject("Happy Anniversary with Restaurant ERP! 🎊")
                    .build();

                request.addVariable("discount", discount + "%");
                request.addVariable("years", years);
                request.addVariable("restaurant", "Restaurant ERP");
                request.addVariable("expiryDate", LocalDate.now().plusDays(7).toString());

                boolean sent = emailService.sendMarketingEmail(request);

                if (sent) {
                    log.info("✓ Anniversary email sent successfully to: {}", recipientEmail);
                    return new ResponseEntity<>(
                        "✓ Anniversary email sent successfully to: " + recipientEmail,
                        HttpStatus.OK
                    );
                } else {
                    log.error("✗ emailService.sendMarketingEmail returned false for: {}", recipientEmail);
                    return new ResponseEntity<>(
                        "✗ Failed to send anniversary email to: " + recipientEmail,
                        HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }
            }

            List<CustomerDto> customers = customerRepository.findCustomersWithAnniversaryOn(LocalDate.now());
            long successCount = 0;
            long failCount = 0;

            for (CustomerDto customer : customers) {
                if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                    continue; // skip customers without email
                }
                Integer commEmail = customer.getCommunicationEmail();
                if (commEmail != null && commEmail == 0) {
                    continue; // respect communication preference
                }
                if (customer.getCreatedAt() == null) {
                    continue; // cannot compute years
                }

                int yearsWithUs = Math.max(Period.between(customer.getCreatedAt(), LocalDate.now()).getYears(), 1);
                String name = (customer.getFirstName() != null && !customer.getFirstName().isEmpty())
                        ? customer.getFirstName()
                        : "Valued Customer";

                try {
                    String html = buildAnniversaryHtml(name, yearsWithUs, discount, activeMsg.get());
                    String subject = "🎉 Happy Anniversary with Restaurant ERP!";
                    emailService.sendEmailToCustomer(customer.getEmail().trim(), subject, html);
                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    log.warn("Failed to send anniversary email to {}: {}", customer.getEmail(), ex.getMessage());
                }
            }

            if (successCount == 0) {
                log.warn("Anniversary broadcast: no emails were sent ({} skipped/fail)", failCount);
                return new ResponseEntity<>(
                    "No customers with valid anniversary data to send emails",
                    HttpStatus.BAD_REQUEST
                );
            }

            log.info("Anniversary broadcast completed - success: {}, failed: {}", successCount, failCount);
            return new ResponseEntity<>(
                "Anniversary emails sent. Success: " + successCount + ", Failed: " + failCount,
                HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("Exception in sendAnniversaryEmail: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending anniversary email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-anniversary-request")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send anniversary email (request body)",
               description = "Send an anniversary email with special discount offer using JSON request body")
    public ResponseEntity<?> sendAnniversaryEmailRequest(@RequestBody MarketingEmailRequest request) {
        try {
            if (request == null || request.getRecipientEmail() == null || request.getRecipientEmail().trim().isEmpty()) {
                log.warn("Anniversary email request rejected: recipientEmail is missing");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            String recipientName = request.getRecipientName() != null && !request.getRecipientName().trim().isEmpty()
                ? request.getRecipientName()
                : "Valued Customer";

            String discount = "20";
            String years = "5";
            if (request.getTemplateVariables() != null) {
                if (request.getTemplateVariables().containsKey("discount")) {
                    discount = request.getTemplateVariables().get("discount");
                }
                if (request.getTemplateVariables().containsKey("years")) {
                    years = request.getTemplateVariables().get("years");
                }
            }

            log.info("Send anniversary email - Email: {}, Name: {}, Discount: {}, Years: {}", request.getRecipientEmail(), recipientName, discount, years);

            MarketingEmailRequest emailRequest = MarketingEmailRequest.builder()
                .recipientEmail(request.getRecipientEmail().trim())
                .recipientName(recipientName.trim())
                .templateType("anniversary")
                .subject("Happy Anniversary with Restaurant ERP! 🎊")
                .build();

            emailRequest.addVariable("discount", discount + "%");
            emailRequest.addVariable("years", years);
            emailRequest.addVariable("restaurant", "Restaurant ERP");
            emailRequest.addVariable("expiryDate", LocalDate.now().plusDays(7).toString());

            boolean sent = emailService.sendMarketingEmail(emailRequest);

            if (sent) {
                log.info("✓ Anniversary email sent successfully to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✓ Anniversary email sent successfully to: " + request.getRecipientEmail(),
                    HttpStatus.OK
                );
            } else {
                log.error("✗ Failed to send anniversary email to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✗ Failed to send anniversary email to: " + request.getRecipientEmail(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendAnniversaryEmailRequest: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending anniversary email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-promotional")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send promotional email",
               description = "Send a promotional email with custom discount")
    public ResponseEntity<?> sendPromotionalEmail(
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(required = false) String recipientName,
            @RequestParam(required = false) String discount) {
        try {
            if (discount == null || discount.trim().isEmpty()) {
                discount = "15";
            }

            log.info("Send promotional email - Email: {}, Name: {}, Discount: {}", recipientEmail, recipientName, discount);

            if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                log.warn("Promotional email request rejected: recipientEmail is missing or empty");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            if (recipientName == null || recipientName.trim().isEmpty()) {
                recipientName = "Valued Customer";
            }

            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail.trim())
                .recipientName(recipientName.trim())
                .templateType("promotional")
                .subject("Special Promotional Offer - " + discount + "% Off!")
                .build();

            request.addVariable("discount", discount + "% discount on all items");
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                log.info("✓ Promotional email sent successfully to: {}", recipientEmail);
                return new ResponseEntity<>(
                    "✓ Promotional email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                log.error("✗ emailService.sendMarketingEmail returned false for: {}", recipientEmail);
                return new ResponseEntity<>(
                    "✗ Failed to send promotional email to: " + recipientEmail,
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendPromotionalEmail: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending promotional email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-promotional-request")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send promotional email (request body)",
               description = "Send a promotional email with custom discount using JSON request body")
    public ResponseEntity<?> sendPromotionalEmailRequest(@RequestBody MarketingEmailRequest request) {
        try {
            if (request == null || request.getRecipientEmail() == null || request.getRecipientEmail().trim().isEmpty()) {
                log.warn("Promotional email request rejected: recipientEmail is missing");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            String recipientName = request.getRecipientName() != null && !request.getRecipientName().trim().isEmpty()
                ? request.getRecipientName()
                : "Valued Customer";

            String discount = "15";
            if (request.getTemplateVariables() != null && request.getTemplateVariables().containsKey("discount")) {
                discount = request.getTemplateVariables().get("discount");
            }

            log.info("Send promotional email - Email: {}, Name: {}, Discount: {}", request.getRecipientEmail(), recipientName, discount);

            MarketingEmailRequest emailRequest = MarketingEmailRequest.builder()
                .recipientEmail(request.getRecipientEmail().trim())
                .recipientName(recipientName.trim())
                .templateType("promotional")
                .subject("Special Promotional Offer - " + discount + "% Off!")
                .build();

            emailRequest.addVariable("discount", discount + "% discount on all items");
            emailRequest.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(emailRequest);

            if (sent) {
                log.info("✓ Promotional email sent successfully to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✓ Promotional email sent successfully to: " + request.getRecipientEmail(),
                    HttpStatus.OK
                );
            } else {
                log.error("✗ Failed to send promotional email to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✗ Failed to send promotional email to: " + request.getRecipientEmail(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendPromotionalEmailRequest: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending promotional email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send test email",
               description = "Send a test email to verify email configuration")
    public ResponseEntity<?> sendTestEmail(
            @RequestParam(required = false) String recipientEmail) {
        try {
            log.info("Send test email - Email: {}", recipientEmail);

            if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                log.warn("Test email request rejected: recipientEmail is missing or empty");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail.trim())
                .recipientName("Test User")
                .templateType("promotional")
                .subject("Test Email - Restaurant ERP")
                .build();

            request.addVariable("discount", "50% OFF");
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                log.info("✓ Test email sent successfully to: {}", recipientEmail);
                return new ResponseEntity<>(
                    "✓ Test email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                log.error("✗ emailService.sendMarketingEmail returned false for: {}", recipientEmail);
                return new ResponseEntity<>(
                    "✗ Failed to send test email to: " + recipientEmail,
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendTestEmail: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending test email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/test-request")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send test email (request body)",
               description = "Send a test email using JSON request body")
    public ResponseEntity<?> sendTestEmailRequest(@RequestBody MarketingEmailRequest request) {
        try {
            if (request == null || request.getRecipientEmail() == null || request.getRecipientEmail().trim().isEmpty()) {
                log.warn("Test email request rejected: recipientEmail is missing");
                return new ResponseEntity<>(
                    "Error: recipientEmail is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            log.info("Send test email - Email: {}", request.getRecipientEmail());

            MarketingEmailRequest emailRequest = MarketingEmailRequest.builder()
                .recipientEmail(request.getRecipientEmail().trim())
                .recipientName("Test User")
                .templateType("promotional")
                .subject("Test Email - Restaurant ERP")
                .build();

            emailRequest.addVariable("discount", "50% OFF");
            emailRequest.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(emailRequest);

            if (sent) {
                log.info("✓ Test email sent successfully to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✓ Test email sent successfully to: " + request.getRecipientEmail(),
                    HttpStatus.OK
                );
            } else {
                log.error("✗ Failed to send test email to: {}", request.getRecipientEmail());
                return new ResponseEntity<>(
                    "✗ Failed to send test email to: " + request.getRecipientEmail(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Exception in sendTestEmailRequest: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                "Error sending test email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String buildBirthdayHtml(String name, String discount, AutomatedMessage activeMsg) {
        String template = emailTemplateService.getBirthdayEmailTemplate();
        String message = buildMessageBody(activeMsg, name, "", LocalDate.now());
        template = template.replace("Enjoy our exclusive {{discount}}% birthday discount on your next visit!", message);

        Map<String, String> variables = new HashMap<>();
        variables.put("name", name != null ? name : "Valued Customer");
        String discountValue = activeMsg.hasDiscount() ? String.format("%.0f", activeMsg.getOfferValue()) : discount;
        variables.put("discount", discountValue != null ? discountValue.replace("%", "") : "25");
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", LocalDate.now().plusDays(7).toString());

        return emailTemplateService.renderTemplate(template, variables);
    }

    private String buildMessageBody(AutomatedMessage message, String firstName, String lastName, LocalDate eventDate) {
        String template = message.getTemplateBody();
        if (template == null || template.trim().isEmpty()) {
            template = "Dear {firstName}, enjoy your special day!";
        }
        return template
                .replace("{firstName}", firstName != null && !firstName.isBlank() ? firstName : "Valued Customer")
                .replace("{lastName}", lastName != null ? lastName : "")
                .replace("{eventDate}", eventDate != null ? eventDate.toString() : LocalDate.now().toString())
                .replace("{offerValue}", message.getOfferValue() != null ? String.format("%.0f", message.getOfferValue()) : "");
    }

    private String buildAnniversaryHtml(String name, int yearsWithUs, String discount, AutomatedMessage activeMsg) {
        String template = emailTemplateService.getAnniversaryEmailTemplate();
        String message = buildMessageBody(activeMsg, name, "", LocalDate.now());
        template = template.replace("Thank you for {{years}} wonderful years! Here's {{discount}}% off for our valued customer.", message);

        Map<String, String> variables = new HashMap<>();
        variables.put("name", name != null ? name : "Valued Customer");
        variables.put("years", String.valueOf(yearsWithUs));
        String discountValue = activeMsg.hasDiscount() ? String.format("%.0f", activeMsg.getOfferValue()) : discount;
        variables.put("discount", discountValue != null ? discountValue.replace("%", "") : "20");
        variables.put("restaurant", "Restaurant ERP");
        variables.put("expiryDate", LocalDate.now().plusDays(7).toString());

        return emailTemplateService.renderTemplate(template, variables);
    }

    // ========== Scheduler Config Endpoints ==========
    @GetMapping("/scheduler")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getSchedulerConfig() {
        try {
            return ResponseEntity.ok(emailSchedulerConfigService.getCurrentConfig());
        } catch (Exception e) {
            log.error("Error fetching scheduler config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching scheduler config");
        }
    }

    @PutMapping("/scheduler")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateSchedulerConfig(@RequestBody EmailSchedulerConfigDto request) {
        try {
            if (request.getSendTime() == null) {
                return ResponseEntity.badRequest().body("sendTime is required in HH:mm:ss format");
            }
            log.info("Updating email scheduler time to {}", request.getSendTime());
            return ResponseEntity.ok(emailSchedulerConfigService.updateSendTime(request.getSendTime()));
        } catch (Exception e) {
            log.error("Error updating scheduler config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating scheduler config");
        }
    }

    @PostMapping("/send-to-all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Send email to all customers",
               description = "Send a promotional email to all customers with valid email preferences")
    public ResponseEntity<?> sendToAllCustomers() {
        try {
            log.info("=== Send Email to All Customers Request ===");

            List<CustomerDto> customers = customerRepository.getAllCustomers();
            if (customers == null || customers.isEmpty()) {
                log.warn("No customers found in database");
                Map<String, Object> response = new HashMap<>();
                response.put("status", 200);
                response.put("message", "No customers found in database");
                response.put("emailsSent", 0);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            log.info("Found {} customers in database", customers.size());

            long successCount = 0;
            long failCount = 0;
            long skippedCount = 0;

            for (CustomerDto customer : customers) {
                try {

                    if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                        log.debug("⊘ Customer {} skipped: no email address", customer.getId());
                        skippedCount++;
                        continue;
                    }

                    Integer commEmail = customer.getCommunicationEmail();
                    if (commEmail != null && commEmail == 0) {
                        log.debug("⊘ Customer {} skipped: email communication disabled", customer.getId());
                        skippedCount++;
                        continue;
                    }

                    String name = (customer.getFirstName() != null && !customer.getFirstName().isEmpty())
                            ? customer.getFirstName()
                            : "Valued Customer";

                    String subject = "Special Offer for You!";
                    String template = emailTemplateService.getPromotionalEmailTemplate();

                    Map<String, String> variables = new HashMap<>();
                    variables.put("name", name);
                    variables.put("discount", "20");
                    variables.put("restaurant", "Restaurant ERP");
                    variables.put("expiryDate", LocalDate.now().plusDays(7).toString());

                    String htmlBody = emailTemplateService.renderTemplate(template, variables);

                    emailService.sendEmailToCustomer(customer.getEmail().trim(), subject, htmlBody);
                    successCount++;
                    log.info("✓ Email sent to {} ({})", name, customer.getEmail());

                } catch (Exception ex) {
                    failCount++;
                    log.error("✗ Failed to send email to customer {}: {}", customer.getId(), ex.getMessage());
                }
            }

            log.info("✓ Email broadcast completed - Success: {}, Failed: {}, Skipped: {}", successCount, failCount, skippedCount);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Emails sent to all eligible customers");
            response.put("emailsSent", successCount);
            response.put("failed", failCount);
            response.put("skipped", skippedCount);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception in sendToAllCustomers: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 500);
            errorResponse.put("message", "Error sending emails");
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
