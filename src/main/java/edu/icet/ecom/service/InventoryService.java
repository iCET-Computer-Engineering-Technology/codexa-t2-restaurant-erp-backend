package edu.icet.ecom.service;

import edu.icet.ecom.dto.InventoryDeductionResponse;
import edu.icet.ecom.dto.LowStockAlertDto;
import edu.icet.ecom.dto.StockDiscrepancyDto;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryService {
    InventoryDeductionResponse handleFiredStatus(Integer orderItemId);
    List<StockDiscrepancyDto> getDiscrepancyReport(Integer sessionId);
    List<LowStockAlertDto> getActiveLowStockAlerts();
    void updateLowStockThreshold(Integer ingredientId, BigDecimal threshold);
}
