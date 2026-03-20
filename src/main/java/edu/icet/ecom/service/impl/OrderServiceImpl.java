package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderDto;
import edu.icet.ecom.dto.OrderItemDto;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.OrderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // Valid order types matching the DB ENUM
    private static final Set<String> VALID_ORDER_TYPES = Set.of("dine_in", "takeout", "delivery", "online");

    // Valid statuses matching the DB ENUM
    private static final Set<String> VALID_STATUSES = Set.of("open", "sent_to_kitchen", "partially_ready", "ready", "paid", "voided");

    private static final BigDecimal TAX_RATE     = new BigDecimal("0.10");
    private static final BigDecimal SERVICE_RATE = new BigDecimal("0.15");

    @Override
    @Transactional
    public OrderDto createOrder(@NonNull OrderDto orderDto) {
        //Validation
        if(orderDto.getItems() == null || orderDto.getItems().isEmpty()){
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if(orderDto.getOrderType() == null || !VALID_ORDER_TYPES.contains(orderDto.getOrderType())) {
            throw new IllegalArgumentException("Invalid order type");
        }
        if ("dine_in".equals(orderDto.getOrderType()) && orderDto.getTableId() == null){
            throw new IllegalArgumentException("Table ID is required for dine_in orders");
        }
        if(!"dine_in".equals(orderDto.getOrderType())){
            orderDto.setTableId(null);
        }
        //Validate items
        for (OrderItemDto orderItemDto : orderDto.getItems()){
            if(orderItemDto.getMenuItemId() == null){
                throw new IllegalArgumentException("Menu item ID is required for each item");
            }
            if(orderItemDto.getPortionId() == null) {
                throw new IllegalArgumentException("Portion ID is required for each item");
            }
            if(orderItemDto.getQuantity() == null || orderItemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0 for each item");
            }
            if(orderItemDto.getPrice() == null || orderItemDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0 for each item");
            }
        }
        //Calculate subtotal
        BigDecimal subtotal = orderDto.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = subtotal.multiply(TAX_RATE);
        BigDecimal serviceCharge = subtotal.multiply(SERVICE_RATE);
        BigDecimal totalAmount = subtotal.add(taxAmount).add(serviceCharge).subtract(discountAmount);

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setOrderType(orderDto.getOrderType());
        order.setTableId(orderDto.getTableId());
        order.setCustomerId(orderDto.getCustomerId());
        order.setServerId(orderDto.getServerId());
        order.setStatus("open");
        order.setSubTotal(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setTaxAmount(taxAmount);
        order.setServiceCharge(serviceCharge);
        order.setTotalAmount(totalAmount);
        order.setNotes(orderDto.getNotes());
        order.setCreatedAt(orderDto.getCreatedAt() != null ? orderDto.getCreatedAt().toLocalDateTime() : LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Integer orderId = orderRepository.saveAndGetId(order);
        order.setId(orderId);

        //Save each
        List<OrderItem> orderItemsArray = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setMenuItemId(orderItemDto.getMenuItemId());
            orderItem.setPortionId(orderItemDto.getPortionId());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setStatus("pending");
            orderItem.setNotes(orderItemDto.getNotes());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setId(orderItemRepository.saveAndGetId(orderItem));
            orderItemsArray.add(orderItem);
        }
        return toDto(order, orderItemsArray);
    }

    @Override
    public OrderDto findById(Integer id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            return null;
        }
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        return toDto(order, items);
    }

    @Override
    public List<OrderDto> findByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Must be one of: " + VALID_STATUSES);
        }
        return orderRepository.findByStatus(status).stream()
                .map(order -> toDto(order,
                        orderItemRepository.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> toDto(order,
                        orderItemRepository.findByOrderId(order.getId())))
                .toList();
    }

    @Override
    public Boolean updateStatus(Integer orderId, String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status +". Must be one of: " + VALID_STATUSES);
        }
        boolean updated = orderRepository.updateStatus(orderId, status);
        if (!updated)
            throw new RuntimeException("Order not found: id=" + orderId);
        return true;
    }


    //Generate order num : ORD-20260318-0001
    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        int sequence = orderRepository.upsertAndGetSequence(today);
        String date = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%04d", sequence);
    }

    //map order to dto
    private OrderDto toDto(Order order, List<OrderItem> items) {

        List<OrderItemDto> orderItemDtoList= items.stream().map(item -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(item.getId());
            orderItemDto.setOrderId(item.getOrderId());
            orderItemDto.setMenuItemId(item.getMenuItemId());
            orderItemDto.setPortionId(item.getPortionId());
            orderItemDto.setQuantity(item.getQuantity());
            orderItemDto.setPrice(item.getPrice());
            orderItemDto.setLineTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItemDto.setStatus(item.getStatus());
            orderItemDto.setNotes(item.getNotes());
            if (item.getCreatedAt() != null) {
                orderItemDto.setCreatedAt(Timestamp.valueOf(item.getCreatedAt()));
            }
            return orderItemDto;
        }).toList();

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setOrderType(order.getOrderType());
        orderDto.setTableId(order.getTableId());
        orderDto.setCustomerId(order.getCustomerId());
        orderDto.setServerId(order.getServerId());
        orderDto.setStatus(order.getStatus());
        orderDto.setSubTotal(order.getSubTotal());
        orderDto.setDiscountAmount(order.getDiscountAmount());
        orderDto.setTaxAmount(order.getTaxAmount());
        orderDto.setServiceCharge(order.getServiceCharge());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setNotes(order.getNotes());
        // Add null-safety checks for order timestamps
        if (order.getCreatedAt() != null) {
            orderDto.setCreatedAt(Timestamp.valueOf(order.getCreatedAt()));
        }
        if (order.getUpdatedAt() != null) {
            orderDto.setUpdatedAt(Timestamp.valueOf(order.getUpdatedAt()));
        }
        orderDto.setItems(orderItemDtoList);
        return orderDto;
    }
}
