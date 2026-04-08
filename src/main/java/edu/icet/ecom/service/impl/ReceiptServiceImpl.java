package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ReceiptDTO;
import edu.icet.ecom.dto.ReceiptItemDTO;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.PaymentRepository;
import edu.icet.ecom.repository.ReceiptRepository;
import edu.icet.ecom.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<ReceiptDTO> getAllPaidOrders() {
        List<Order> orders = receiptRepository.findAllPaidOrders();
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all order IDs
        List<Integer> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        // Batch fetch payments for all orders at once (1 query, not N)
        List<Map<String, Object>> paymentRows = paymentRepository.findPaymentsByOrderIds(orderIds);
        Map<Integer, Map<String, Object>> paymentMap = new HashMap<>();
        for (Map<String, Object> row : paymentRows) {
            Integer orderId = (Integer) row.get("order_id");
            paymentMap.put(orderId, row);
        }

        // Batch fetch items for all orders at once (1 query, not N)
        List<Map<String, Object>> itemRows = orderItemRepository.findItemsWithNamesByOrderIds(orderIds);
        Map<Integer, List<Map<String, Object>>> itemMap = new HashMap<>();
        for (Map<String, Object> row : itemRows) {
            Integer orderId = (Integer) row.get("order_id");
            itemMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(row);
        }

        // Map to DTOs
        return orders.stream()
                .map(order -> mapToReceiptDTO(
                        order,
                        paymentMap.get(order.getId()),
                        itemMap.getOrDefault(order.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptDTO getReceiptById(Integer orderId) {
        Order order = receiptRepository.findPaidOrderById(orderId);
        if (order == null) {
            throw new RuntimeException(
                    "Receipt not found for order id: " + orderId + " (order may not be paid)"
            );
        }

        Map<String, Object> paymentRow = paymentRepository.findPaymentWithUserByOrderId(orderId);
        List<Map<String, Object>> itemRows = orderItemRepository.findItemsWithNamesByOrderId(orderId);

        return mapToReceiptDTO(order, paymentRow, itemRows);
    }

    @Override
    public List<ReceiptDTO> searchReceipts(String orderNumber, LocalDate startDate, LocalDate endDate) {
        // Pass null if blank string to match the SQL IS NULL check
        String orderNumberParam = (orderNumber != null && !orderNumber.isBlank())
                ? orderNumber : null;

        List<Order> orders = receiptRepository.searchPaidOrders(
                orderNumberParam, startDate, endDate
        );

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        List<Map<String, Object>> paymentRows = paymentRepository.findPaymentsByOrderIds(orderIds);
        Map<Integer, Map<String, Object>> paymentMap = new HashMap<>();
        for (Map<String, Object> row : paymentRows) {
            paymentMap.put((Integer) row.get("order_id"), row);
        }

        List<Map<String, Object>> itemRows = orderItemRepository.findItemsWithNamesByOrderIds(orderIds);
        Map<Integer, List<Map<String, Object>>> itemMap = new HashMap<>();
        for (Map<String, Object> row : itemRows) {
            itemMap.computeIfAbsent((Integer) row.get("order_id"), k -> new ArrayList<>()).add(row);
        }

        return orders.stream()
                .map(o -> mapToReceiptDTO(
                        o,
                        paymentMap.get(o.getId()),
                        itemMap.getOrDefault(o.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    // ── PRIVATE MAPPING HELPERS ──────────────────────────────

    private ReceiptDTO mapToReceiptDTO(Order order, Map<String, Object> payment, List<Map<String, Object>> items) {
        ReceiptDTO dto = new ReceiptDTO();

        // From orders table (via existing Order entity fields)
        dto.setOrderId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderType(order.getOrderType());
        dto.setTableId(order.getTableId());
        dto.setStatus(order.getStatus());
        dto.setSubTotal(order.getSubTotal());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setServiceCharge(order.getServiceCharge());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());

        // From payments query result
        if (payment != null) {
            dto.setPaymentMethod(payment.get("payment_method") != null 
                ? payment.get("payment_method").toString() : null);
            dto.setPaymentAmount(payment.get("amount") != null
                ? (BigDecimal) payment.get("amount") : null);
            dto.setTipAmount(payment.get("tip_amount") != null
                ? (BigDecimal) payment.get("tip_amount") : BigDecimal.ZERO);
            dto.setReferenceNumber(payment.get("reference_number") != null 
                ? payment.get("reference_number").toString() : null);
            dto.setProcessedBy(payment.get("processed_by_username") != null 
                ? payment.get("processed_by_username").toString() : null);
            dto.setProcessedAt(payment.get("processed_at") != null
                ? (LocalDateTime) payment.get("processed_at") : null);
        }

        // From order_items JOIN query
        List<ReceiptItemDTO> itemDTOs = items.stream().map(row -> {
            ReceiptItemDTO item = new ReceiptItemDTO();
            item.setId(row.get("id") != null ? (Integer) row.get("id") : null);
            item.setItemName(row.get("item_name") != null 
                ? row.get("item_name").toString() : "Unknown");
            item.setPortionName(row.get("portion_name") != null 
                ? row.get("portion_name").toString() : null);
            item.setQuantity(row.get("quantity") != null ? (Integer) row.get("quantity") : 0);
            item.setPrice(row.get("price") != null 
                ? (BigDecimal) row.get("price") : BigDecimal.ZERO);
            item.setLineTotal(row.get("line_total") != null 
                ? (BigDecimal) row.get("line_total") : BigDecimal.ZERO);
            item.setNotes(row.get("notes") != null ? row.get("notes").toString() : null);
            return item;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}
