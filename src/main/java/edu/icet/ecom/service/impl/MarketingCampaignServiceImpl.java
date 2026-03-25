package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MarketingCampaignDto;
import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.entity.MarketingCampaign;
import edu.icet.ecom.repository.MarketingCampaignRepository;
import edu.icet.ecom.service.MarketingCampaignService;
import edu.icet.ecom.service.CustomerService;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.EmailTemplateService;
import edu.icet.ecom.service.CampaignAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketingCampaignServiceImpl implements MarketingCampaignService {

    private final MarketingCampaignRepository marketingCampaignRepository;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
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
        log.warn("STARTING CAMPAIGN EMAIL SENDING");
        log.warn("Campaign ID: {}", campaignId);

        try {
            MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                    .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

            log.info("✓ Campaign fetched: {} | Status: {} | Subject: {}",
                    campaign.getCampaignName(), campaign.getStatus(), campaign.getSubject());

            List<CustomerDto> customers = customerService.getAllCustomer();
            log.info("✓ Total customers in system: {}", customers.size());

            if (campaign.getSegmentId() != null) {
                customers = customers.stream()
                        .filter(c -> c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                        .collect(Collectors.toList());
            } else {
                customers = customers.stream()
                        .filter(c -> c.getCommunicationEmail() != null && c.getCommunicationEmail() == 1)
                        .collect(Collectors.toList());
            }

            log.info("✓ Customers opted-in for email (communication_email=1): {}", customers.size());

            if (customers.isEmpty()) {
                log.warn("⚠️  No customers to send to!");
            }

            int successCount = 0;
            int failureCount = 0;

            for (CustomerDto customer : customers) {
                try {
                    String variant = assignVariant(campaignId, customer.getId());
                    String emailBody = buildHtmlEmailBody(campaign, customer, variant);

                    log.debug("Sending to: {} | Name: {} | Variant: {}",
                            customer.getEmail(), customer.getFirstName(), variant);

                    emailService.sendEmailToCustomer(customer.getEmail(), campaign.getSubject(), emailBody);
                    campaignAnalyticsService.recordCampaignSent(campaignId, customer.getId(), variant);

                    successCount++;
                    log.info("✓ Email sent to: {}", customer.getEmail());

                } catch (Exception e) {
                    failureCount++;
                    log.error("✗ Error sending email to customer {} ({}): {}",
                            customer.getId(), customer.getEmail(), e.getMessage(), e);
                }
            }

            log.warn("EMAIL SENDING SUMMARY");
            log.warn("Total sent: {} | Failed: {}", successCount, failureCount);

            markCampaignAsSent(campaignId);

            log.warn("CAMPAIGN EMAIL SENDING COMPLETED");
            log.warn("Campaign: {}", campaign.getCampaignName());
            log.warn("tatus: SENT");

        } catch (Exception e) {
            log.error("CAMPAIGN SENDING FAILED");
            log.error("Campaign ID: {} | Error: {}", campaignId, e.getMessage());
            throw e;
        }
    }

    private String buildHtmlEmailBody(MarketingCampaign campaign, CustomerDto customer, String variant) {
        String template = emailTemplateService.getMarketingCampaignTemplate();
        String campaignMessage = variant.equals("A") ? campaign.getBodyTemplate() : campaign.getVariantBBody();
        if (campaignMessage == null || campaignMessage.trim().isEmpty()) {
            campaignMessage = campaign.getBodyTemplate();
        }

        Map<String, String> variables = new HashMap<>();
        variables.put("name", customer.getFirstName() != null ? customer.getFirstName() : "Valued Customer");
        variables.put("campaignName", campaign.getCampaignName());
        variables.put("message", campaignMessage);
        variables.put("discount", extractDiscount(campaign));
        variables.put("restaurant", "Restaurant ERP");
        variables.put("campaignId", campaign.getId().toString());

        String renderedTemplate = emailTemplateService.renderTemplate(template, variables);
        log.debug("Campaign email template rendered for customer: {}", customer.getEmail());
        return renderedTemplate;
    }

    private String extractDiscount(MarketingCampaign campaign) {
        String body = campaign.getBodyTemplate();
        if (body != null && body.contains("%")) {
            int percentIndex = body.indexOf("%");
            if (percentIndex > 0) {
                int startIndex = Math.max(0, percentIndex - 3);
                String discount = body.substring(startIndex, percentIndex + 1);
                return discount.replaceAll("[^0-9%]", "");
            }
        }
        return "Special Offer";
    }

    private String assignVariant(Integer campaignId, Integer customerId) {
        int hash = (campaignId.hashCode() + customerId.hashCode());
        return (hash % 2) == 0 ? "A" : "B";
    }

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
