package edu.icet.ecom.config;

import edu.icet.ecom.service.AutomatedMessageSchedulerService;
import edu.icet.ecom.service.EmailSchedulerConfigService;
import edu.icet.ecom.service.MarketingCampaignService;
import edu.icet.ecom.dto.MarketingCampaignDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutomatedEmailSchedulingConfig {

    private final EmailSchedulerConfigService schedulerConfigService;
    private final AutomatedMessageSchedulerService automatedMessageSchedulerService;
    private final MarketingCampaignService marketingCampaignService;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Scheduled(fixedRate = 60000) // Run every 60 seconds (1 minute)
    public void checkAndExecuteScheduledEmails() {
        try {
            String configuredTime = schedulerConfigService.getConfiguredTimeAsString();

            String currentTime = LocalTime.now().format(TIME_FORMATTER);

            log.debug("Checking scheduled emails: Current time = {}, Configured time = {}", currentTime, configuredTime);

            if (currentTime != null && configuredTime != null && currentTime.equals(configuredTime)) {
                log.info("GLOBAL SCHEDULER TRIGGERED - EXECUTING AUTOMATED EMAILS");
                log.info("Time: {} on {}", currentTime, LocalDate.now());
                automatedMessageSchedulerService.runScheduledAutomations();
                log.info("GLOBAL SCHEDULER EXECUTION COMPLETED");
            }
        } catch (Exception e) {
            log.error("Error in scheduled email check", e);
        }
    }

    @Scheduled(fixedRate = 60000) // Run every 60 seconds (1 minute)
    public void checkAndExecuteScheduledCampaigns() {
        try {
            log.warn("CAMPAIGN SCHEDULER CHECK - Starting");

            LocalTime now = LocalTime.now();
            String currentTimeStr = now.format(TIME_FORMATTER);
            LocalDate today = LocalDate.now();

            log.warn("Current time: {} | Current date: {}", currentTimeStr, today);

            var allCampaigns = marketingCampaignService.getAllCampaigns();
            log.warn("Total campaigns in database: {}", allCampaigns.size());

            var campaignsToBeSent = allCampaigns
                    .stream()
                    .filter(c -> {
                        if (c.getScheduledAt() == null) {
                            return false;
                        }
                        LocalDate campaignDate = c.getScheduledAt().toLocalDate();
                        String campaignStatus = c.getStatus();

                        boolean isToday = campaignDate.equals(today);
                        boolean isScheduled = campaignStatus != null &&
                                (campaignStatus.equalsIgnoreCase("scheduled") ||
                                 campaignStatus.equals("SCHEDULED"));

                        if (isToday && isScheduled) {
                            log.warn("Found: Campaign ID={} | Name='{}' | Time={} | Status={}",
                                    c.getId(), c.getCampaignName(),
                                    c.getScheduledAt().format(TIME_FORMATTER), campaignStatus);
                        }
                        return isToday && isScheduled;
                    })
                    .toList();

            log.warn("Campaigns scheduled for today: {}", campaignsToBeSent.size());

            if (campaignsToBeSent.isEmpty()) {
                log.warn("No scheduled campaigns found for today");
                return;
            }

            for (MarketingCampaignDto campaign : campaignsToBeSent) {
                try {
                    String campaignTimeStr = campaign.getScheduledAt().format(TIME_FORMATTER);
                    log.warn("Processing Campaign ID: {} | Name: '{}' | Scheduled: {} | Current: {}",
                            campaign.getId(), campaign.getCampaignName(), campaignTimeStr, currentTimeStr);
                    log.warn("Campaign Status: {} | Subject: {}", campaign.getStatus(), campaign.getSubject());

                    boolean timeMatches = currentTimeStr.equals(campaignTimeStr);

                    if (timeMatches) {
                        log.warn("✓✓✓ TIME MATCH DETECTED! ✓✓✓");
                        log.warn("Sending campaign: {}", campaign.getCampaignName());

                        marketingCampaignService.sendCampaignToCustomers(campaign.getId());

                        log.warn("✓✓✓ CAMPAIGN SENT SUCCESSFULLY! ✓✓✓");
                    } else {
                        log.debug("Time mismatch - Scheduled: {} | Current: {} (no action)",
                                campaignTimeStr, currentTimeStr);
                    }
                } catch (Exception e) {
                    log.error("✗ FAILED to send campaign '{}': {}",
                            campaign.getCampaignName(), e.getMessage(), e);
                }
            }

            log.warn("CAMPAIGN SCHEDULER CHECK - Complete");

        } catch (Exception e) {
            log.error("CRITICAL ERROR in campaign scheduler: {}", e.getMessage(), e);
        }
    }
}

