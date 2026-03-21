package edu.icet.ecom.service;

import edu.icet.ecom.dto.CampaignAnalyticsDto;
import edu.icet.ecom.dto.CampaignPerformanceMetricsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignAnalyticsService {

    CampaignAnalyticsDto recordCampaignSent(Integer campaignId, Integer customerId, String variant);
    void recordCampaignOpen(Integer campaignId, Integer customerId);
    void recordCampaignClick(Integer campaignId, Integer customerId);
    void recordCampaignConversion(Integer campaignId, Integer customerId);
    void recordUnsubscribe(Integer campaignId, Integer customerId);
    CampaignPerformanceMetricsDto getCampaignPerformanceMetrics(Integer campaignId);
    List<CampaignPerformanceMetricsDto> getAllCampaignMetrics();
    CampaignPerformanceMetricsDto getCampaignMetricsByDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate);
    List<CampaignAnalyticsDto> getCampaignAnalytics(Integer campaignId);
    List<CampaignAnalyticsDto> getCustomerCampaignAnalytics(Integer campaignId, Integer customerId);
    void deleteAnalyticsByCampaignId(Integer campaignId);
}

