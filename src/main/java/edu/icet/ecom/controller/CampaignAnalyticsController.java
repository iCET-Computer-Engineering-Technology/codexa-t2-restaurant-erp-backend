package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CampaignAnalyticsDto;
import edu.icet.ecom.dto.CampaignPerformanceMetricsDto;
import edu.icet.ecom.service.CampaignAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/campaign-analytics")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CampaignAnalyticsController {

    private final CampaignAnalyticsService campaignAnalyticsService;

    @PostMapping("/{campaignId}/record-sent")
    public ResponseEntity<?> recordCampaignSent(
            @PathVariable Integer campaignId,
            @RequestParam Integer customerId,
            @RequestParam(defaultValue = "A") String variant) {
        try {
            CampaignAnalyticsDto result = campaignAnalyticsService.recordCampaignSent(campaignId, customerId, variant);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording campaign sent: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{campaignId}/record-open")
    public ResponseEntity<?> recordCampaignOpen(
            @PathVariable Integer campaignId,
            @RequestParam Integer customerId) {
        try {
            campaignAnalyticsService.recordCampaignOpen(campaignId, customerId);
            return new ResponseEntity<>("Open event recorded", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording open: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{campaignId}/record-click")
    public ResponseEntity<?> recordCampaignClick(
            @PathVariable Integer campaignId,
            @RequestParam Integer customerId) {
        try {
            campaignAnalyticsService.recordCampaignClick(campaignId, customerId);
            return new ResponseEntity<>("Click event recorded", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording click: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{campaignId}/record-conversion")
    public ResponseEntity<?> recordCampaignConversion(
            @PathVariable Integer campaignId,
            @RequestParam Integer customerId) {
        try {
            campaignAnalyticsService.recordCampaignConversion(campaignId, customerId);
            return new ResponseEntity<>("Conversion event recorded", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording conversion: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{campaignId}/record-unsubscribe")
    public ResponseEntity<?> recordUnsubscribe(
            @PathVariable Integer campaignId,
            @RequestParam Integer customerId) {
        try {
            campaignAnalyticsService.recordUnsubscribe(campaignId, customerId);
            return new ResponseEntity<>("Unsubscribe event recorded", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording unsubscribe: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{campaignId}/metrics")
    public ResponseEntity<?> getCampaignPerformanceMetrics(@PathVariable Integer campaignId) {
        try {
            CampaignPerformanceMetricsDto metrics = campaignAnalyticsService.getCampaignPerformanceMetrics(campaignId);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching metrics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/metrics/all")
    public ResponseEntity<?> getAllCampaignMetrics() {
        try {
            List<CampaignPerformanceMetricsDto> metrics = campaignAnalyticsService.getAllCampaignMetrics();
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching all metrics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{campaignId}/metrics/date-range")
    public ResponseEntity<?> getCampaignMetricsByDateRange(
            @PathVariable Integer campaignId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            CampaignPerformanceMetricsDto metrics = campaignAnalyticsService.getCampaignMetricsByDateRange(campaignId, start, end);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching metrics: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{campaignId}/all")
    public ResponseEntity<?> getCampaignAnalytics(@PathVariable Integer campaignId) {
        try {
            List<CampaignAnalyticsDto> analytics = campaignAnalyticsService.getCampaignAnalytics(campaignId);
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching analytics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{campaignId}/customer/{customerId}")
    public ResponseEntity<?> getCustomerCampaignAnalytics(
            @PathVariable Integer campaignId,
            @PathVariable Integer customerId) {
        try {
            List<CampaignAnalyticsDto> analytics = campaignAnalyticsService.getCustomerCampaignAnalytics(campaignId, customerId);
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching analytics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{campaignId}/delete-analytics")
    public ResponseEntity<?> deleteAnalyticsByCampaignId(@PathVariable Integer campaignId) {
        try {
            campaignAnalyticsService.deleteAnalyticsByCampaignId(campaignId);
            return new ResponseEntity<>("Analytics deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting analytics: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

