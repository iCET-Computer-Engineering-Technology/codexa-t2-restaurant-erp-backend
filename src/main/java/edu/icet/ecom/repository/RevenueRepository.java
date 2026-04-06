package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ReconciliationReportDto;
import edu.icet.ecom.dto.RevenueSummaryDto;
import java.time.LocalDate;
import java.util.List;

public interface RevenueRepository {
    List<RevenueSummaryDto> getRevenueByChannel(LocalDate date);
    List<ReconciliationReportDto> getDailyReconciliation(LocalDate date);
}
