package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MarketingEmailRequest;
import edu.icet.ecom.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Email Management", description = "API endpoints for email operations")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-marketing")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Send birthday email",
               description = "Send a birthday email with special discount offer")
    public ResponseEntity<?> sendBirthdayEmail(
            @RequestParam String recipientEmail,
            @RequestParam String recipientName,
            @RequestParam(defaultValue = "25") String discount) {
        try {
            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail)
                .recipientName(recipientName)
                .templateType("birthday")
                .subject("Happy Birthday, " + recipientName + "! 🎉")
                .build();

            request.addVariable("discount", discount);
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                return new ResponseEntity<>(
                    "Birthday email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    "Failed to send birthday email",
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Error sending birthday email", e);
            return new ResponseEntity<>(
                "Error sending birthday email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-anniversary")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Send anniversary email",
               description = "Send an anniversary email with special discount offer")
    public ResponseEntity<?> sendAnniversaryEmail(
            @RequestParam String recipientEmail,
            @RequestParam String recipientName,
            @RequestParam(defaultValue = "20") String discount,
            @RequestParam(defaultValue = "5") String years) {
        try {
            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail)
                .recipientName(recipientName)
                .templateType("anniversary")
                .subject("Happy Anniversary with Restaurant ERP! 🎊")
                .build();

            request.addVariable("discount", discount);
            request.addVariable("years", years);
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                return new ResponseEntity<>(
                    "Anniversary email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    "Failed to send anniversary email",
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Error sending anniversary email", e);
            return new ResponseEntity<>(
                "Error sending anniversary email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/send-promotional")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Send promotional email",
               description = "Send a promotional email with custom discount")
    public ResponseEntity<?> sendPromotionalEmail(
            @RequestParam String recipientEmail,
            @RequestParam String recipientName,
            @RequestParam(defaultValue = "15") String discount) {
        try {
            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail)
                .recipientName(recipientName)
                .templateType("promotional")
                .subject("Special Promotional Offer - " + discount + "% Off!")
                .build();

            request.addVariable("discount", discount + "% discount on all items");
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                return new ResponseEntity<>(
                    "Promotional email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    "Failed to send promotional email",
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Error sending promotional email", e);
            return new ResponseEntity<>(
                "Error sending promotional email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Send test email",
               description = "Send a test email to verify email configuration")
    public ResponseEntity<?> sendTestEmail(@RequestParam String recipientEmail) {
        try {
            MarketingEmailRequest request = MarketingEmailRequest.builder()
                .recipientEmail(recipientEmail)
                .recipientName("Test User")
                .templateType("promotional")
                .subject("Test Email - Restaurant ERP")
                .build();

            request.addVariable("discount", "50% OFF");
            request.addVariable("restaurant", "Restaurant ERP");

            boolean sent = emailService.sendMarketingEmail(request);

            if (sent) {
                return new ResponseEntity<>(
                    "Test email sent successfully to: " + recipientEmail,
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    "Failed to send test email",
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (Exception e) {
            log.error("Error sending test email", e);
            return new ResponseEntity<>(
                "Error sending test email: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}

