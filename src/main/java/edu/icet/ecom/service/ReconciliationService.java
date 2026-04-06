package edu.icet.ecom.service;

import java.time.LocalDate;

public interface ReconciliationService {
    void runAutomaticReconciliation();
    void performDailyReconciliation(LocalDate date);
}