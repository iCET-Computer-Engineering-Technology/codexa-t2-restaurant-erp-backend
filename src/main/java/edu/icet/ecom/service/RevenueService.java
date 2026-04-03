package edu.icet.ecom.service;

import edu.icet.ecom.dto.ReconciliationReportDto;
import edu.icet.ecom.dto.RevenueSummaryDto;

import java.time.LocalDate;
import java.util.List;

public interface RevenueService {
    List<RevenueSummaryDto> getChannelPerformance(LocalDate date);
    List<ReconciliationReportDto> getDailyReconciliation(LocalDate date);
}
