package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignAnalytics {
    private Integer id;
    private Integer campaignId;
    private Integer customerId;
    private LocalDateTime sentAt;
    private LocalDateTime openedAt;
    private LocalDateTime clickedAt;
    private LocalDateTime convertedAt;
    private LocalDateTime unsubscribedAt;
    private String variant;

    public CampaignAnalytics(Integer campaignId, Integer customerId, LocalDateTime sentAt, String variant) {
        this.campaignId = campaignId;
        this.customerId = customerId;
        this.sentAt = sentAt;
        this.variant = variant;
    }
}

