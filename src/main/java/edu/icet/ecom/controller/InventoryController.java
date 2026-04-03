package edu.icet.ecom.controller;

import edu.icet.ecom.dto.StockDiscrepancyDto;
import edu.icet.ecom.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}

