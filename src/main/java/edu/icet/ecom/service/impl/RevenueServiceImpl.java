package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ReconciliationReportDto;
import edu.icet.ecom.dto.RevenueSummaryDto;
import edu.icet.ecom.repository.RevenueRepository;
import edu.icet.ecom.service.RevenueService;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {
    private final RevenueRepository revenueRepository;

    public RevenueServiceImpl(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    @Override
    public List<RevenueSummaryDto> getChannelPerformance(LocalDate date) {
        return revenueRepository.getRevenueByChannel(date);
    }

    @Override
    public List<ReconciliationReportDto> getDailyReconciliation(LocalDate date) {
        List<ReconciliationReportDto> reports = revenueRepository.getDailyReconciliation(date);
        reports.forEach(report -> {
            if (!"Matched".equals(report.getStatus())) {
                // උදාහරණයක් විලසින්: Log discrepancy details or send notification
                System.out.println("ALERT: Discrepancy found on " + date + " Amount: " + report.getDiscrepancy());
            }
        });

        return reports;
    }
}
