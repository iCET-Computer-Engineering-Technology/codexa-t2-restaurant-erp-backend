package edu.icet.ecom.controller;

import edu.icet.ecom.dto.LowStockAlertDto;
import edu.icet.ecom.dto.LowStockThresholdUpdateRequest;
import edu.icet.ecom.dto.StockDiscrepancyDto;
import edu.icet.ecom.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/discrepancy-report")
    public List<StockDiscrepancyDto> getDiscrepancyReport(@RequestParam(required = false) Integer sessionId) {
        return inventoryService.getDiscrepancyReport(sessionId);
    }

    @GetMapping("/low-stock-alerts")
    public List<LowStockAlertDto> getLowStockAlerts() {
        return inventoryService.getActiveLowStockAlerts();
    }

    @PatchMapping("/ingredients/{ingredientId}/low-stock-threshold")
    public void updateLowStockThreshold(@PathVariable Integer ingredientId,
                                        @RequestBody LowStockThresholdUpdateRequest request) {
        inventoryService.updateLowStockThreshold(ingredientId, request.getThreshold());
    }
}
