package edu.icet.ecom.service;

import edu.icet.ecom.dto.MarketingCampaignDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketingCampaignService {

    MarketingCampaignDto createCampaign(MarketingCampaignDto campaignDto);
    List<MarketingCampaignDto> getAllCampaigns();
    MarketingCampaignDto getCampaignById(Integer id);
    MarketingCampaignDto updateCampaign(Integer id, MarketingCampaignDto campaignDto);
    boolean deleteCampaign(Integer id);
    List<MarketingCampaignDto> getCampaignsByStatus(String status);
    List<MarketingCampaignDto> getCampaignsBySegment(Integer segmentId);
    List<MarketingCampaignDto> getCampaignsByChannel(String channel);
    List<MarketingCampaignDto> searchCampaigns(String name);
    void scheduleCampaign(Integer campaignId, LocalDateTime scheduledDate);
    void markCampaignAsSent(Integer campaignId);
    void cancelCampaign(Integer campaignId);
    void sendCampaignToCustomers(Integer campaignId);
}

