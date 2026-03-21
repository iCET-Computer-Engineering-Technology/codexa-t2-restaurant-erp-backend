package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CampaignPerformanceMetricsDto {
    private Integer campaignId;
    private String campaignName;
    private Long totalSent;
    private Long totalOpened;
    private Long totalClicked;
    private Long totalConverted;
    private Long totalUnsubscribed;
    private Double openRate; // (opened / sent) * 100
    private Double clickRate; // (clicked / sent) * 100
    private Double conversionRate; // (converted / sent) * 100
    private Double unsubscribeRate; // (unsubscribed / sent) * 100
    private String channel;
    private String status;

    // Variant specific metrics
    private Long variantAOpened;
    private Long variantBOpened;
    private Long variantAConverted;
    private Long variantBConverted;
    private Double variantAConversionRate;
    private Double variantBConversionRate;
    private Double variantAOpenRate;
    private Double variantBOpenRate;

    public void calculateRates() {
        if (totalSent > 0) {
            this.openRate = (totalOpened.doubleValue() / totalSent.doubleValue()) * 100;
            this.clickRate = (totalClicked.doubleValue() / totalSent.doubleValue()) * 100;
            this.conversionRate = (totalConverted.doubleValue() / totalSent.doubleValue()) * 100;
            this.unsubscribeRate = (totalUnsubscribed.doubleValue() / totalSent.doubleValue()) * 100;
        }
    }
}

