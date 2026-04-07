package edu.icet.ecom.repository;

import edu.icet.ecom.dto.StockDiscrepancyDto;

import java.util.List;

public interface StockCountSessionRepository {
    List<StockDiscrepancyDto> getDiscrepancyReport(Integer sessionId);
}

