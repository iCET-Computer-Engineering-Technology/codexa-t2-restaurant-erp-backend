package edu.icet.ecom.repository;

import edu.icet.ecom.entity.MarketingCampaign;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarketingCampaignRepository {

    Integer save(MarketingCampaign campaign);
    boolean update(MarketingCampaign campaign);
    Optional<MarketingCampaign> findById(Integer id);
    List<MarketingCampaign> findAll();
    List<MarketingCampaign> findByStatus(String status);
    List<MarketingCampaign> findBySegmentId(Integer segmentId);
    List<MarketingCampaign> findByChannel(String channel);
    List<MarketingCampaign> findByCreatedBy(Integer userId);
    List<MarketingCampaign> findByCampaignNameContaining(String name);
    List<MarketingCampaign> findByScheduledDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MarketingCampaign> findAllSentCampaigns();
    List<MarketingCampaign> findActiveCampaigns();
    boolean deleteById(Integer id);
}


