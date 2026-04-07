package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ReconciliationDto;
import java.time.LocalDate;

public interface ReconciliationRepository {
    void saveReconciliation(ReconciliationDto dto);
    Double getTotalOrderAmountForDate(LocalDate date);
    Double getTotalPaymentAmountForDate(LocalDate date);
}
