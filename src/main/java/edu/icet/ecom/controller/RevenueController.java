package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ReconciliationReportDto;
import edu.icet.ecom.service.CustomerService;
import edu.icet.ecom.service.RevenueService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Revenue")
@CrossOrigin
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/reconcile")
    public ResponseEntity<List<ReconciliationReportDto>> getReconciliationReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(revenueService.getDailyReconciliation(date));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportReconciliationReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok().build();
    }

}
