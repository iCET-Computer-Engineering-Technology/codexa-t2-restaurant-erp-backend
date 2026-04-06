package edu.icet.ecom.controller;

import edu.icet.ecom.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    @PostMapping("/run")
    public String runReconciliation() {
        reconciliationService.runAutomaticReconciliation();
        return "Reconciliation process triggered.";
    }

    @GetMapping("/daily")
    public String performDailyReconciliation() {
        reconciliationService.performDailyReconciliation(java.time.LocalDate.now().minusDays(1));
        return "Daily reconciliation process triggered for yesterday.";
    }
}
