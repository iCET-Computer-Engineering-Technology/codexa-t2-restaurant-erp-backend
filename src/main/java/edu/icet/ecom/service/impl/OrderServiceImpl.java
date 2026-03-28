package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.*;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // Valid order types matching the DB ENUM
    private static final Set<String> VALID_ORDER_TYPES = Set.of("dine_in", "takeout", "booking");

    private static final String ORDER_TYPE_DINE_IN = "dine_in";
    private static final String ORDER_TYPE_TAKEOUT = "takeout";
    private static final String ORDER_TYPE_BOOKING = "booking";

    private static final Map<String, String> ORDER_TYPE_ALIASES = Map.ofEntries(
            Map.entry("dine_in", ORDER_TYPE_DINE_IN),
            Map.entry("dine-in", ORDER_TYPE_DINE_IN),
            Map.entry("dinein", ORDER_TYPE_DINE_IN),
            Map.entry("takeout", ORDER_TYPE_TAKEOUT),
            Map.entry("take_out", ORDER_TYPE_TAKEOUT),
            Map.entry("booking", ORDER_TYPE_BOOKING),
            Map.entry("online", ORDER_TYPE_BOOKING),
            Map.entry("call", ORDER_TYPE_BOOKING)
    );

    // Valid statuses matching the DB ENUM
    private static final Set<String> VALID_STATUSES = Set.of("open", "sent_to_kitchen", "partially_ready", "ready", "paid", "voided");

    private static final BigDecimal TAX_RATE     = new BigDecimal("0.00");
    private static final BigDecimal SERVICE_RATE = new BigDecimal("0.00");

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        String normalizedOrderType = normalizeOrderType(request == null ? null : request.getOrderType());
        validateCreateRequest(request, normalizedOrderType);

        BigDecimal subtotal = request.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = subtotal.multiply(TAX_RATE);
        BigDecimal serviceCharge = subtotal.multiply(SERVICE_RATE);
        BigDecimal totalAmount = subtotal.add(taxAmount).add(serviceCharge).subtract(discountAmount);

        Order order = new Order();
        order.setOrderTypeId(request.getOrderTypeId());
        order.setOrderNumber(generateOrderNumber());
        order.setOrderType(normalizedOrderType);
        order.setTableId(ORDER_TYPE_TAKEOUT.equals(normalizedOrderType) ? null : request.getTableId());
        order.setCustomerId(request.getCustomerId());
        order.setServerId(request.getServerId());
        order.setStatus("open");
        order.setSubTotal(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setTaxAmount(taxAmount);
        order.setServiceCharge(serviceCharge);
        order.setTotalAmount(totalAmount);
        order.setNotes(request.getNotes());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Integer orderId = orderRepository.saveAndGetId(order);
        order.setId(orderId);

        List<OrderItem> savedItems = new ArrayList<>();
        for (OrderItemCreateRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setMenuItemId(itemReq.getMenuItemId());
            item.setPortionId(itemReq.getPortionId());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(itemReq.getPrice());
            item.setStatus("pending");
            item.setNotes(itemReq.getNotes());
            item.setCreatedAt(LocalDateTime.now());
            item.setId(orderItemRepository.saveAndGetId(item));
            savedItems.add(item);
        }
        return mapToResponse(order, savedItems);
    }

    @Override
    public OrderResponse findById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found: id=" + id);
        }
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        return mapToResponse(order, items);
    }

    @Override
    public List<OrderResponse> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("status is required");
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Must be one of: " + VALID_STATUSES);
        }
        return orderRepository.findByStatus(status).stream()
                .map(o -> mapToResponse(o, orderItemRepository.findByOrderId(o.getId())))
                .toList();
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(o -> mapToResponse(o, orderItemRepository.findByOrderId(o.getId())))
                .toList();
    }

    @Override
    public Boolean updateStatus(Integer orderId, String status) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("status is required");
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Must be one of: " + VALID_STATUSES);
        }
        boolean updated = orderRepository.updateStatus(orderId, status);
        if (!updated) {
            throw new ResourceNotFoundException("Order not found: id=" + orderId);
        }
        return true;
    }

    @Override
    public Boolean updateType(Integer orderId, String type) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }

        Order existing = orderRepository.findById(orderId);
        if (existing == null) {
            throw new ResourceNotFoundException("Order not found: id=" + orderId);
        }

        String normalizedType = normalizeOrderType(type);
        validateOrderTypeRequirements(normalizedType, existing.getTableId(), existing.getCustomerId(), existing.getServerId());

        boolean updated = orderRepository.updateType(orderId, normalizedType);
        if (!updated) {
            throw new ResourceNotFoundException("Order not found: id=" + orderId);
        }
        return true;
    }

    private void validateCreateRequest(OrderCreateRequest request, String normalizedOrderType) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }

        validateOrderTypeRequirements(normalizedOrderType, request.getTableId(), request.getCustomerId(), request.getServerId());

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        for (OrderItemCreateRequest item : request.getItems()) {
            if (item == null) throw new IllegalArgumentException("items must not contain null elements");
            if (item.getMenuItemId() == null) throw new IllegalArgumentException("menuItemId is required");
            if (item.getPortionId() == null) throw new IllegalArgumentException("portionId is required");
            if (item.getQuantity() == null || item.getQuantity() <= 0) throw new IllegalArgumentException("quantity must be >= 1");
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("price must be > 0");
        }
    }

    private void validateOrderTypeRequirements(String normalizedOrderType, Integer tableId, Integer customerId, Integer serverId) {
        if (!VALID_ORDER_TYPES.contains(normalizedOrderType)) {
            throw new IllegalArgumentException("Invalid orderType. Must be one of: " + VALID_ORDER_TYPES);
        }

        if (serverId == null || serverId <= 0) {
            throw new IllegalArgumentException("serverId is required");
        }

        if (ORDER_TYPE_DINE_IN.equals(normalizedOrderType) || ORDER_TYPE_BOOKING.equals(normalizedOrderType)) {
            if (tableId == null || tableId <= 0) {
                throw new IllegalArgumentException("tableId is required for dine_in and booking orders");
            }
        }

        if (ORDER_TYPE_TAKEOUT.equals(normalizedOrderType) && tableId != null) {
            throw new IllegalArgumentException("tableId must be omitted for takeout orders");
        }

        if (ORDER_TYPE_BOOKING.equals(normalizedOrderType) && (customerId == null || customerId <= 0)) {
            throw new IllegalArgumentException("customerId is required for booking orders");
        }
    }

    private String normalizeOrderType(String rawType) {
        if (rawType == null || rawType.trim().isEmpty()) {
            throw new IllegalArgumentException("orderType is required");
        }
        String normalized = rawType.trim().toLowerCase(Locale.ROOT);
        return ORDER_TYPE_ALIASES.getOrDefault(normalized, normalized);
    }

    //Generate order num : ORD-20260318-0001
    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        int sequence = orderRepository.upsertAndGetSequence(today);
        String date = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%04d", sequence);
    }

    private OrderResponse mapToResponse(Order order, List<OrderItem> items) {
        List<OrderItem> safeItems = items == null ? List.of() : items;
        List<OrderItemResponse> itemResponses = safeItems.stream().map(i -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setId(i.getId());
            r.setOrderId(i.getOrderId());
            r.setMenuItemId(i.getMenuItemId());
            r.setPortionId(i.getPortionId());
            r.setQuantity(i.getQuantity());
            r.setPrice(i.getPrice());
            if (i.getPrice() != null && i.getQuantity() != null) {
                r.setLineTotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
            }
            r.setStatus(i.getStatus());
            r.setNotes(i.getNotes());
            r.setCreatedAt(i.getCreatedAt());
            return r;
        }).toList();
        return getOrderResponse(order, itemResponses);
    }

    private static @NonNull OrderResponse getOrderResponse(Order order, List<OrderItemResponse> itemResponses) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderTypeId(order.getOrderTypeId());
        response.setOrderNumber(order.getOrderNumber());
        response.setOrderType(order.getOrderType());
        response.setTableId(order.getTableId());
        response.setCustomerId(order.getCustomerId());
        response.setServerId(order.getServerId());
        response.setStatus(order.getStatus());
        response.setSubTotal(order.getSubTotal());
        response.setDiscountAmount(order.getDiscountAmount());
        response.setTaxAmount(order.getTaxAmount());
        response.setServiceCharge(order.getServiceCharge());
        response.setTotalAmount(order.getTotalAmount());
        response.setNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setItems(itemResponses);
        return response;
    }
}
