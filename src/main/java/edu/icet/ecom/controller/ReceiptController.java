package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ReceiptDTO;
import edu.icet.ecom.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
@CrossOrigin
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        return ResponseEntity.ok(receiptService.getAllPaidOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> getReceiptById(@PathVariable Integer id) {
        return ResponseEntity.ok(receiptService.getReceiptById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReceiptDTO>> searchReceipts(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                receiptService.searchReceipts(orderNumber, startDate, endDate)
        );
    }
}
