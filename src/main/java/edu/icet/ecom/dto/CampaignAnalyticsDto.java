package edu.icet.ecom.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CampaignAnalyticsDto {
    private Integer id;
    private Integer campaignId;
    private Integer customerId;
    private LocalDateTime sentAt;
    private LocalDateTime openedAt;
    private LocalDateTime clickedAt;
    private LocalDateTime convertedAt;
    private LocalDateTime unsubscribedAt;
    private String variant;
}

