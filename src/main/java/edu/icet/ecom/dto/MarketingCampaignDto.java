package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MarketingCampaignDto {
    private Integer id;
    @NotBlank(message = "Campaign name cannot be empty")
    private String campaignName;
    private Integer segmentId;
    @NotBlank(message = "Channel cannot be empty")
    private String channel; // 'email' or 'sms'
    private String subject;
    private String bodyTemplate;
    private Boolean abTestEnabled;
    private String variantBBody;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private String status; // 'draft', 'scheduled', 'sent', 'cancelled'
    private Integer createdBy;
    private LocalDateTime createdAt;
}

