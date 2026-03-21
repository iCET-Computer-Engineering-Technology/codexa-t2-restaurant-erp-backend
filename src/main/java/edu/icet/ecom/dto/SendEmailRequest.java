package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    private String subject;
    private String message;
    private String recipientType; // "all", "byBirthday", "byAnniversary", or specific customer email
}

