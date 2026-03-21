package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketingEmailRequest {

    private String recipientEmail;
    private String recipientName;
    private String templateType; // "promotional", "birthday", "anniversary", "campaign"
    private Map<String, String> templateVariables; // placeholders to replace
    private String subject;
    private String expiryDate;

    public void addVariable(String key, String value) {
        if (this.templateVariables == null) {
            this.templateVariables = new HashMap<>();
        }
        this.templateVariables.put(key, value);
    }

    public Map<String, String> getTemplateVariables() {
        if (this.templateVariables == null) {
            this.templateVariables = new HashMap<>();
        }

        if (this.recipientName != null && !this.templateVariables.containsKey("name")) {
            this.templateVariables.put("name", this.recipientName);
        }

        if (!this.templateVariables.containsKey("restaurant")) {
            this.templateVariables.put("restaurant", "Restaurant ERP");
        }
        return this.templateVariables;
    }
}

