package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ReconciliationDto;
import edu.icet.ecom.repository.ReconciliationRepository;
import edu.icet.ecom.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReconciliationServiceImpl implements ReconciliationService {

    private final ReconciliationRepository reconciliationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void runAutomaticReconciliation() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        performDailyReconciliation(yesterday);
    }

    @Override
    public void performDailyReconciliation(LocalDate date) {
        Double totalOrders = reconciliationRepository.getTotalOrderAmountForDate(date);
        Double totalPayments = reconciliationRepository.getTotalPaymentAmountForDate(date);
        double discrepancy = totalOrders - totalPayments;
        String status = (Math.abs(discrepancy) < 0.01) ? "MATCHED" : "DISCREPANCY";

        ReconciliationDto dto = new ReconciliationDto(date, totalOrders, totalPayments, discrepancy, status);
        reconciliationRepository.saveReconciliation(dto);

        System.out.println("Reconciliation finished for: " + date + " | Status: " + status);
    }
}
