package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CampaignAnalyticsDto;
import edu.icet.ecom.dto.CampaignPerformanceMetricsDto;
import edu.icet.ecom.entity.CampaignAnalytics;
import edu.icet.ecom.entity.MarketingCampaign;
import edu.icet.ecom.repository.CampaignAnalyticsRepository;
import edu.icet.ecom.repository.MarketingCampaignRepository;
import edu.icet.ecom.service.CampaignAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignAnalyticsServiceImpl implements CampaignAnalyticsService {

    private final CampaignAnalyticsRepository campaignAnalyticsRepository;
    private final MarketingCampaignRepository marketingCampaignRepository;

    @Override
    public CampaignAnalyticsDto recordCampaignSent(Integer campaignId, Integer customerId, String variant) {
        CampaignAnalytics analytics = CampaignAnalytics.builder()
                .campaignId(campaignId)
                .customerId(customerId)
                .sentAt(LocalDateTime.now())
                .variant(variant)
                .build();

        Integer id = campaignAnalyticsRepository.save(analytics);
        analytics.setId(id);
        return convertToDto(analytics);
    }

    @Override
    public void recordCampaignOpen(Integer campaignId, Integer customerId) {
        List<CampaignAnalytics> analyticsList = campaignAnalyticsRepository
                .findByCampaignIdAndCustomerId(campaignId, customerId);

        for (CampaignAnalytics analytics : analyticsList) {
            if (analytics.getOpenedAt() == null) {
                analytics.setOpenedAt(LocalDateTime.now());
                campaignAnalyticsRepository.save(analytics);
            }
        }
    }

    @Override
    public void recordCampaignClick(Integer campaignId, Integer customerId) {
        List<CampaignAnalytics> analyticsList = campaignAnalyticsRepository
                .findByCampaignIdAndCustomerId(campaignId, customerId);

        for (CampaignAnalytics analytics : analyticsList) {
            if (analytics.getClickedAt() == null) {
                analytics.setClickedAt(LocalDateTime.now());
                campaignAnalyticsRepository.save(analytics);
            }
        }
    }

    @Override
    public void recordCampaignConversion(Integer campaignId, Integer customerId) {
        List<CampaignAnalytics> analyticsList = campaignAnalyticsRepository
                .findByCampaignIdAndCustomerId(campaignId, customerId);

        for (CampaignAnalytics analytics : analyticsList) {
            if (analytics.getConvertedAt() == null) {
                analytics.setConvertedAt(LocalDateTime.now());
                campaignAnalyticsRepository.save(analytics);
            }
        }
    }

    @Override
    public void recordUnsubscribe(Integer campaignId, Integer customerId) {
        List<CampaignAnalytics> analyticsList = campaignAnalyticsRepository
                .findByCampaignIdAndCustomerId(campaignId, customerId);

        for (CampaignAnalytics analytics : analyticsList) {
            if (analytics.getUnsubscribedAt() == null) {
                analytics.setUnsubscribedAt(LocalDateTime.now());
                campaignAnalyticsRepository.save(analytics);
            }
        }
    }

    @Override
    public CampaignPerformanceMetricsDto getCampaignPerformanceMetrics(Integer campaignId) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        CampaignPerformanceMetricsDto metrics = new CampaignPerformanceMetricsDto();
        metrics.setCampaignId(campaignId);
        metrics.setCampaignName(campaign.getCampaignName());
        metrics.setChannel(campaign.getChannel().toString());
        metrics.setStatus(campaign.getStatus().toString());

        // Get counts from repository
        Long totalSent = campaignAnalyticsRepository.countTotalSentByCampaignId(campaignId);
        Long totalOpened = campaignAnalyticsRepository.countOpenedByCampaignId(campaignId);
        Long totalClicked = campaignAnalyticsRepository.countClickedByCampaignId(campaignId);
        Long totalConverted = campaignAnalyticsRepository.countConvertedByCampaignId(campaignId);
        Long totalUnsubscribed = campaignAnalyticsRepository.countUnsubscribedByCampaignId(campaignId);

        metrics.setTotalSent(totalSent);
        metrics.setTotalOpened(totalOpened);
        metrics.setTotalClicked(totalClicked);
        metrics.setTotalConverted(totalConverted);
        metrics.setTotalUnsubscribed(totalUnsubscribed);

        // Get variant specific metrics if A/B testing is enabled
        if (Boolean.TRUE.equals(campaign.getAbTestEnabled())) {
            Long variantAOpened = campaignAnalyticsRepository.countVariantAOpened(campaignId);
            Long variantBOpened = campaignAnalyticsRepository.countVariantBOpened(campaignId);
            Long variantAConverted = campaignAnalyticsRepository.countVariantAConverted(campaignId);
            Long variantBConverted = campaignAnalyticsRepository.countVariantBConverted(campaignId);

            metrics.setVariantAOpened(variantAOpened);
            metrics.setVariantBOpened(variantBOpened);
            metrics.setVariantAConverted(variantAConverted);
            metrics.setVariantBConverted(variantBConverted);

            // Calculate variant conversion rates
            Long variantATotal = countVariantTotal(campaignId, "A");
            Long variantBTotal = countVariantTotal(campaignId, "B");

            if (variantATotal > 0) {
                metrics.setVariantAConversionRate((variantAConverted.doubleValue() / variantATotal.doubleValue()) * 100);
                metrics.setVariantAOpenRate((variantAOpened.doubleValue() / variantATotal.doubleValue()) * 100);
            }

            if (variantBTotal > 0) {
                metrics.setVariantBConversionRate((variantBConverted.doubleValue() / variantBTotal.doubleValue()) * 100);
                metrics.setVariantBOpenRate((variantBOpened.doubleValue() / variantBTotal.doubleValue()) * 100);
            }
        }

        // Calculate rates
        metrics.calculateRates();

        return metrics;
    }

    @Override
    public List<CampaignPerformanceMetricsDto> getAllCampaignMetrics() {
        List<MarketingCampaign> campaigns = marketingCampaignRepository.findAll();
        return campaigns.stream()
                .map(campaign -> getCampaignPerformanceMetrics(campaign.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public CampaignPerformanceMetricsDto getCampaignMetricsByDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        CampaignPerformanceMetricsDto metrics = new CampaignPerformanceMetricsDto();
        metrics.setCampaignId(campaignId);
        metrics.setCampaignName(campaign.getCampaignName());
        metrics.setChannel(campaign.getChannel().toString());
        metrics.setStatus(campaign.getStatus().toString());

        // Get counts by date range
        Long totalSent = campaignAnalyticsRepository.countSentByDateRange(campaignId, startDate, endDate);
        List<CampaignAnalytics> analyticsList = campaignAnalyticsRepository.findByCampaignIdAndDateRange(campaignId, startDate, endDate);

        Long totalOpened = analyticsList.stream().filter(a -> a.getOpenedAt() != null).count();
        Long totalClicked = analyticsList.stream().filter(a -> a.getClickedAt() != null).count();
        Long totalConverted = analyticsList.stream().filter(a -> a.getConvertedAt() != null).count();
        Long totalUnsubscribed = analyticsList.stream().filter(a -> a.getUnsubscribedAt() != null).count();

        metrics.setTotalSent(totalSent);
        metrics.setTotalOpened(totalOpened);
        metrics.setTotalClicked(totalClicked);
        metrics.setTotalConverted(totalConverted);
        metrics.setTotalUnsubscribed(totalUnsubscribed);

        // Calculate rates
        metrics.calculateRates();

        return metrics;
    }

    @Override
    public List<CampaignAnalyticsDto> getCampaignAnalytics(Integer campaignId) {
        return campaignAnalyticsRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignAnalyticsDto> getCustomerCampaignAnalytics(Integer campaignId, Integer customerId) {
        return campaignAnalyticsRepository.findByCampaignIdAndCustomerId(campaignId, customerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAnalyticsByCampaignId(Integer campaignId) {
        campaignAnalyticsRepository.deleteAllByCampaignId(campaignId);
    }

    // Helper method to convert entity to DTO
    private CampaignAnalyticsDto convertToDto(CampaignAnalytics analytics) {
        return new CampaignAnalyticsDto(
                analytics.getId(),
                analytics.getCampaignId(),
                analytics.getCustomerId(),
                analytics.getSentAt(),
                analytics.getOpenedAt(),
                analytics.getClickedAt(),
                analytics.getConvertedAt(),
                analytics.getUnsubscribedAt(),
                analytics.getVariant()
        );
    }

    // Helper method to count total records for a variant
    private Long countVariantTotal(Integer campaignId, String variant) {
        return campaignAnalyticsRepository.findByCampaignId(campaignId)
                .stream()
                .filter(a -> variant.equals(a.getVariant()))
                .count();
    }
}


