package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderItemModifierRequestDto;
import edu.icet.ecom.dto.OrderItemRequestDto;
import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.entity.OrderItemModifier;
import edu.icet.ecom.exception.OrderPersistenceException;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.service.OrderService;
import edu.icet.ecom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemModifierRepository orderItemModifierRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        //Calculate total amount(items * qty + modifiers)
        BigDecimal subTotal = orderRequestDto.getItems().stream()
                .map(itemDto -> {
                    BigDecimal itemTotal = itemDto.getUnitPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()));

                    BigDecimal modifierTotal = BigDecimal.ZERO;
                    if (itemDto.getModifiers() != null){
                        modifierTotal = itemDto.getModifiers().stream()
                                .map(OrderItemModifierRequestDto::getPriceAdjustment)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    return itemTotal.add(modifierTotal);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        //if we want to add tax : calculation
        BigDecimal discountAmount = BigDecimal.ZERO;      // extend later with discount logic
        BigDecimal taxRate = new BigDecimal("0.00");  // adjust if tax applies
        BigDecimal taxAmount = subTotal.subtract(discountAmount).multiply(taxRate);
        BigDecimal totalAmount = subTotal.subtract(discountAmount).add(taxAmount);

        //save order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());          // uses order_sequence table
        order.setOrderType(orderRequestDto.getOrderType());   // REQUIRED field
        order.setTableId(orderRequestDto.getTableId());
        order.setCustomerId(orderRequestDto.getCustomerId()); // nullable
        order.setServerId(orderRequestDto.getServerId());     // nullable
        order.setStatus("open");
        order.setSubTotal(subTotal);
        order.setDiscountAmount(discountAmount);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(totalAmount);
        order.setNotes(orderRequestDto.getNotes());
        order.setSource(orderRequestDto.getSource());

        Integer orderId = orderRepository.saveAndGetId(order);

        //save order items
        List<OrderItem> orderItemsArray = new ArrayList<>();

        for (OrderItemRequestDto itemDto : orderRequestDto.getItems()) {

            // Per-item modifier total
            BigDecimal itemModifierTotal = BigDecimal.ZERO;
            if (itemDto.getModifiers() != null) {
                itemModifierTotal = itemDto.getModifiers().stream()
                        .map(OrderItemModifierRequestDto::getPriceAdjustment)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            BigDecimal lineTotal = itemDto.getUnitPrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
                    .add(itemModifierTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setMenuItemId(itemDto.getMenuItemId());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(itemDto.getUnitPrice());
            orderItem.setModifierTotal(itemModifierTotal);
            orderItem.setLineTotal(lineTotal);
            orderItem.setCourseNumber(1);                    // default
            orderItem.setStatus("pending");                  //  default
            orderItem.setNotes(itemDto.getNotes());

            Integer orderItemId = orderItemRepository.saveAndGetId(orderItem);
            orderItem.setId(orderItemId);

            //save modifiers
            if (itemDto.getModifiers() != null) {
                for (OrderItemModifierRequestDto modifierDto : itemDto.getModifiers()) {
                    OrderItemModifier modifier = new OrderItemModifier();
                    modifier.setOrderItemId(orderItemId);
                    modifier.setModifierId(modifierDto.getModifierId());
                    modifier.setModifierName(modifierDto.getModifierName());
                    modifier.setPriceAdjustment(modifierDto.getPriceAdjustment());
                    // Note: No quantity field — removed from new schema
                    int rows = orderItemModifierRepository.save(modifier);
                    if (rows == 0) {
                        throw new OrderPersistenceException(
                                "Failed to save modifier: " + modifier.getModifierName());
                    }
                }
            }
            orderItemsArray.add(orderItem);
        }

        //return response
        return new OrderResponseDto(
                orderId,
                order.getOrderNumber(),
                order.getOrderType(),
                order.getStatus(),
                order.getSubTotal(),
                order.getDiscountAmount(),
                order.getTaxAmount(),
                order.getTotalAmount(),
                order.getSource(),
                orderItemsArray);
    }

    @Override
    public List<OrderResponseDto> findOpenOrders() {
        return orderRepository.findOpenOrders().stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setId(order.getId());
            dto.setOrderNumber(order.getOrderNumber());
            dto.setOrderType(order.getOrderType());
            dto.setStatus(order.getStatus());
            dto.setSubTotal(order.getSubTotal());
            dto.setDiscountAmount(order.getDiscountAmount());
            dto.setTaxAmount(order.getTaxAmount());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setSource(order.getSource());
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            dto.setItems(items);
            return dto;
        }).toList();
    }

    @Override
    public Boolean updateStatus(Integer orderId, String status) {
        // Valid statuses from new schema ENUM
        List<String> validStatuses = List.of(
                "open", "sent_to_kitchen", "partially_ready", "ready", "paid", "voided");
        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status +
                    ". Must be one of: " + validStatuses);
        }
        boolean updated = orderRepository.updateStatus(orderId, status);
        if (!updated) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        return true;
    }


    //Generate order num
    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        int sequence = orderRepository.upsertAndGetSequence(today);
        String date = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%04d", sequence);
    }
}
