package edu.icet.ecom.repository;

import edu.icet.ecom.entity.CampaignAnalytics;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignAnalyticsRepository {

    Integer save(CampaignAnalytics analytics);
    boolean update(CampaignAnalytics analytics);
    List<CampaignAnalytics> findByCampaignId(Integer campaignId);
    List<CampaignAnalytics> findByCampaignIdAndCustomerId(Integer campaignId, Integer customerId);
    Long countOpenedByCampaignId(Integer campaignId);
    Long countClickedByCampaignId(Integer campaignId);
    Long countConvertedByCampaignId(Integer campaignId);
    Long countUnsubscribedByCampaignId(Integer campaignId);
    Long countTotalSentByCampaignId(Integer campaignId);
    Long countVariantAOpened(Integer campaignId);
    Long countVariantBOpened(Integer campaignId);
    Long countVariantAConverted(Integer campaignId);
    Long countVariantBConverted(Integer campaignId);
    List<CampaignAnalytics> findByCampaignIdAndDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate);
    Long countSentByDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate);
    boolean deleteAllByCampaignId(Integer campaignId);
}


