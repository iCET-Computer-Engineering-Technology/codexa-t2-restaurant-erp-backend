package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MarketingCampaignDto;
import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.entity.MarketingCampaign;
import edu.icet.ecom.repository.MarketingCampaignRepository;
import edu.icet.ecom.service.MarketingCampaignService;
import edu.icet.ecom.service.CustomerService;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.CampaignAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketingCampaignServiceImpl implements MarketingCampaignService {

    private final MarketingCampaignRepository marketingCampaignRepository;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final CampaignAnalyticsService campaignAnalyticsService;

    @Override
    public MarketingCampaignDto createCampaign(MarketingCampaignDto campaignDto) {
        MarketingCampaign campaign = new MarketingCampaign();
        campaign.setCampaignName(campaignDto.getCampaignName());
        campaign.setSegmentId(campaignDto.getSegmentId());
        campaign.setChannel(MarketingCampaign.Channel.fromDbValue(campaignDto.getChannel()));
        campaign.setSubject(campaignDto.getSubject());
        campaign.setBodyTemplate(campaignDto.getBodyTemplate());
        campaign.setAbTestEnabled(campaignDto.getAbTestEnabled() != null ? campaignDto.getAbTestEnabled() : false);
        campaign.setVariantBBody(campaignDto.getVariantBBody());
        campaign.setScheduledAt(campaignDto.getScheduledAt());
        campaign.setStatus(MarketingCampaign.Status.DRAFT);
        campaign.setCreatedBy(campaignDto.getCreatedBy());
        campaign.setCreatedAt(LocalDateTime.now());

        Integer id = marketingCampaignRepository.save(campaign);
        campaign.setId(id);
        return convertToDto(campaign);
    }

    @Override
    public List<MarketingCampaignDto> getAllCampaigns() {
        return marketingCampaignRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MarketingCampaignDto getCampaignById(Integer id) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + id));
        return convertToDto(campaign);
    }

    @Override
    public MarketingCampaignDto updateCampaign(Integer id, MarketingCampaignDto campaignDto) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + id));

        campaign.setCampaignName(campaignDto.getCampaignName());
        campaign.setSegmentId(campaignDto.getSegmentId());
        campaign.setChannel(MarketingCampaign.Channel.fromDbValue(campaignDto.getChannel()));
        campaign.setSubject(campaignDto.getSubject());
        campaign.setBodyTemplate(campaignDto.getBodyTemplate());
        campaign.setAbTestEnabled(campaignDto.getAbTestEnabled());
        campaign.setVariantBBody(campaignDto.getVariantBBody());
        campaign.setScheduledAt(campaignDto.getScheduledAt());

        marketingCampaignRepository.update(campaign);
        return convertToDto(campaign);
    }

    @Override
    public boolean deleteCampaign(Integer id) {
        return marketingCampaignRepository.deleteById(id);
    }

    @Override
    public List<MarketingCampaignDto> getCampaignsByStatus(String status) {
        return marketingCampaignRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignDto> getCampaignsBySegment(Integer segmentId) {
        return marketingCampaignRepository.findBySegmentId(segmentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignDto> getCampaignsByChannel(String channel) {
        return marketingCampaignRepository.findByChannel(channel)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignDto> searchCampaigns(String name) {
        return marketingCampaignRepository.findByCampaignNameContaining(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void scheduleCampaign(Integer campaignId, LocalDateTime scheduledDate) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        campaign.setScheduledAt(scheduledDate);
        campaign.setStatus(MarketingCampaign.Status.SCHEDULED);
        marketingCampaignRepository.update(campaign);
    }

    @Override
    public void markCampaignAsSent(Integer campaignId) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        campaign.setStatus(MarketingCampaign.Status.SENT);
        campaign.setSentAt(LocalDateTime.now());
        marketingCampaignRepository.update(campaign);
    }

    @Override
    public void cancelCampaign(Integer campaignId) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        campaign.setStatus(MarketingCampaign.Status.CANCELLED);
        marketingCampaignRepository.update(campaign);
    }

    @Override
    public void sendCampaignToCustomers(Integer campaignId) {
        // Get the campaign
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        // Get all customers
        List<CustomerDto> customers = customerService.getAllCustomers();

        // Filter by segment if specified
        if (campaign.getSegmentId() != null) {
            // In a real implementation, you might filter by segment here
            // For now, we'll send to all customers who can receive emails
            customers = customers.stream()
                    .filter(c -> c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                    .collect(Collectors.toList());
        } else {
            // Send to all customers who can receive emails
            customers = customers.stream()
                    .filter(c -> c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                    .collect(Collectors.toList());
        }

        // Send email to each customer
        for (CustomerDto customer : customers) {
            try {
                // Assign variant for A/B testing
                String variant = assignVariant(campaignId, customer.getId());
                String emailBody = "A".equals(variant) ?
                        campaign.getBodyTemplate() : campaign.getVariantBBody();

                // Send email
                emailService.sendEmailToCustomer(customer.getEmail(), campaign.getSubject(), emailBody);

                // Record the sent event in analytics
                campaignAnalyticsService.recordCampaignSent(campaignId, customer.getId(), variant);

            } catch (Exception e) {
                // Log error but continue sending to other customers
                System.err.println("Error sending email to customer " + customer.getId() + ": " + e.getMessage());
            }
        }

        // Mark campaign as sent
        markCampaignAsSent(campaignId);
    }

    /**
     * Assign variant for A/B testing (50/50 split)
     */
    private String assignVariant(Integer campaignId, Integer customerId) {
        // Create a deterministic variant assignment based on campaign and customer ID
        // This ensures the same customer always gets the same variant
        int hash = (campaignId.hashCode() + customerId.hashCode());
        return (hash % 2) == 0 ? "A" : "B";
    }

    // ...existing code...
    private MarketingCampaignDto convertToDto(MarketingCampaign campaign) {
        MarketingCampaignDto dto = new MarketingCampaignDto();
        dto.setId(campaign.getId());
        dto.setCampaignName(campaign.getCampaignName());
        dto.setSegmentId(campaign.getSegmentId());
        dto.setChannel(campaign.getChannel().getDbValue());
        dto.setSubject(campaign.getSubject());
        dto.setBodyTemplate(campaign.getBodyTemplate());
        dto.setAbTestEnabled(campaign.getAbTestEnabled());
        dto.setVariantBBody(campaign.getVariantBBody());
        dto.setScheduledAt(campaign.getScheduledAt());
        dto.setSentAt(campaign.getSentAt());
        dto.setStatus(campaign.getStatus().getDbValue());
        dto.setCreatedBy(campaign.getCreatedBy());
        dto.setCreatedAt(campaign.getCreatedAt());
        return dto;
    }
}


