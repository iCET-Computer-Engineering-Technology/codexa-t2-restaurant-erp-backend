package edu.icet.ecom.service;

import edu.icet.ecom.dto.ReceiptDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptService {
    List<ReceiptDTO> getAllPaidOrders();
    ReceiptDTO getReceiptById(Integer orderId);
    List<ReceiptDTO> searchReceipts(String orderNumber, LocalDate startDate, LocalDate endDate);
}
