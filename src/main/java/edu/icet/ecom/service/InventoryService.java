package edu.icet.ecom.service;

import edu.icet.ecom.dto.InventoryDeductionResponse;
import edu.icet.ecom.dto.StockDiscrepancyDto;

import java.util.List;

public interface InventoryService {
    InventoryDeductionResponse handleFiredStatus(Integer orderItemId);
    List<StockDiscrepancyDto> getDiscrepancyReport(Integer sessionId);
}
