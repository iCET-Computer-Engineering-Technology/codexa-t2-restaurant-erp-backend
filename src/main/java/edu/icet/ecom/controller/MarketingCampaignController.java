package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MarketingCampaignDto;
import edu.icet.ecom.service.MarketingCampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class MarketingCampaignController {

    private final MarketingCampaignService marketingCampaignService;

    @PostMapping
    public ResponseEntity<?> createCampaign(@RequestBody MarketingCampaignDto campaignDto) {
        try {
            log.info("Creating campaign: {}", campaignDto);

            if (campaignDto.getCampaignName() == null || campaignDto.getCampaignName().trim().isEmpty()) {
                log.error("Campaign name is required");
                return new ResponseEntity<>("Error: Campaign name is required", HttpStatus.BAD_REQUEST);
            }

            if (campaignDto.getChannel() == null || campaignDto.getChannel().trim().isEmpty()) {
                log.error("Channel (email/sms) is required");
                return new ResponseEntity<>("Error: Channel (email/sms) is required", HttpStatus.BAD_REQUEST);
            }

            String channel = campaignDto.getChannel().toLowerCase();
            if (!channel.equals("email") && !channel.equals("sms")) {
                log.error("Invalid channel: {}. Must be 'email' or 'sms'", channel);
                return new ResponseEntity<>("Error: Channel must be 'email' or 'sms'", HttpStatus.BAD_REQUEST);
            }

            if (campaignDto.getSegmentId() == null) {
                log.debug("segmentId is optional and not provided");
            }
            if (campaignDto.getCreatedBy() == null) {
                log.debug("createdBy is optional and not provided");
            }

            MarketingCampaignDto createdCampaign = marketingCampaignService.createCampaign(campaignDto);
            log.info("Campaign created successfully with ID: {}", createdCampaign.getId());
            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument creating campaign: {}", e.getMessage());
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error creating campaign: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error creating campaign: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCampaigns() {
        try {
            List<MarketingCampaignDto> campaigns = marketingCampaignService.getAllCampaigns();
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching campaigns: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaignById(@PathVariable Integer id) {
        try {
            MarketingCampaignDto campaign = marketingCampaignService.getCampaignById(id);
            return new ResponseEntity<>(campaign, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching campaign: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCampaign(@PathVariable Integer id, @RequestBody MarketingCampaignDto campaignDto) {
        try {
            MarketingCampaignDto updatedCampaign = marketingCampaignService.updateCampaign(id, campaignDto);
            return new ResponseEntity<>(updatedCampaign, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating campaign: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Integer id) {
        try {
            boolean deleted = marketingCampaignService.deleteCampaign(id);
            if (deleted) {
                return new ResponseEntity<>("Campaign deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Campaign not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting campaign: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getCampaignsByStatus(@PathVariable String status) {
        try {
            List<MarketingCampaignDto> campaigns = marketingCampaignService.getCampaignsByStatus(status);
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching campaigns: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segment/{segmentId}")
    public ResponseEntity<?> getCampaignsBySegment(@PathVariable Integer segmentId) {
        try {
            List<MarketingCampaignDto> campaigns = marketingCampaignService.getCampaignsBySegment(segmentId);
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching campaigns: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/channel/{channel}")
    public ResponseEntity<?> getCampaignsByChannel(@PathVariable String channel) {
        try {
            List<MarketingCampaignDto> campaigns = marketingCampaignService.getCampaignsByChannel(channel);
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching campaigns: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCampaigns(@RequestParam String name) {
        try {
            List<MarketingCampaignDto> campaigns = marketingCampaignService.searchCampaigns(name);
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching campaigns: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<?> scheduleCampaign(@PathVariable Integer id, @RequestParam String scheduledAt) {
        try {
            LocalDateTime scheduledDate = LocalDateTime.parse(scheduledAt);
            marketingCampaignService.scheduleCampaign(id, scheduledDate);
            return new ResponseEntity<>("Campaign scheduled successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error scheduling campaign: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<?> markCampaignAsSent(@PathVariable Integer id) {
        try {
            // Send campaign emails to all customers
            marketingCampaignService.sendCampaignToCustomers(id);
            return new ResponseEntity<>("Campaign sent to all customers successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error sending campaign: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelCampaign(@PathVariable Integer id) {
        try {
            marketingCampaignService.cancelCampaign(id);
            return new ResponseEntity<>("Campaign cancelled successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error cancelling campaign: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

