package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderItemModifierRequestDto;
import edu.icet.ecom.dto.OrderItemRequestDto;
import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.entity.OrderItemModifier;
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
        //Calculate total amount(items + modifiers)
        BigDecimal subTotal = orderRequestDto.getItems().stream()
                .map(itemDto -> {
                    BigDecimal itemTotal = itemDto.getUnitPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()));

                    BigDecimal modifierTotal = BigDecimal.ZERO;
                    if (itemDto.getModifiers() != null){
                        modifierTotal = itemDto.getModifiers().stream()
                                .map(modifierDto ->
                                        modifierDto.getPriceAdjustment()
                                                .multiply(BigDecimal.valueOf(modifierDto.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    return itemTotal.add(modifierTotal); //item prices + item modifier prices
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        //if we want to add tax : calculation
        BigDecimal taxRate = new BigDecimal("0.00");
        BigDecimal tax = subTotal.multiply(taxRate);
        BigDecimal totalAmount = subTotal.add(tax);

        //save order
        Order order = new Order();
        order.setTableId(orderRequestDto.getTableId());
        order.setCustomerId(orderRequestDto.getCustomerId());
        order.setOrderNumber(generateOrderNumber());
        order.setStatus("RECEIVED");
        order.setTotalAmount(totalAmount);
        order.setTax(tax);
        order.setPaymentStatus("PENDING");

        Long orderId = orderRepository.saveAndGetId(order);

        //save oder items
        List<OrderItem> orderItemsArray = new ArrayList<>();

        for(OrderItemRequestDto itemDto: orderRequestDto.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setMenuItemId(itemDto.getMenuItemId());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(itemDto.getUnitPrice());
            //orderItem.setTotalPrice(itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()))); //duplication erro fix
            BigDecimal modifierTotal = BigDecimal.ZERO;
            if (itemDto.getModifiers() != null) {
                modifierTotal = itemDto.getModifiers().stream()
                        .map(m -> m.getPriceAdjustment()
                                .multiply(BigDecimal.valueOf(m.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            orderItem.setTotalPrice(
                    itemDto.getUnitPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
                            .add(modifierTotal)
            );

            Long orderItemId = orderItemRepository.saveAndGetId(orderItem); //get order item id
            orderItem.setId(orderItemId);

            //save modifiers
            if(itemDto.getModifiers() != null){
                for(OrderItemModifierRequestDto modifierDto: itemDto.getModifiers()){
                    OrderItemModifier modifier = new OrderItemModifier();
                    modifier.setOrderItemId(orderItemId);
                    modifier.setModifierId(modifierDto.getModifierId());
                    modifier.setModifierName(modifierDto.getModifierName());
                    modifier.setPriceAdjustment(modifierDto.getPriceAdjustment());
                    modifier.setQuantity(modifierDto.getQuantity());

                    int rows =  orderItemModifierRepository.save(modifier); //save to db
                    if(rows == 0){
                        throw new RuntimeException("Failed to save modifier: "+ modifier.getModifierName());
                    }
                }
            }
            orderItemsArray.add(orderItem);
        }

        //return response
        OrderResponseDto response = new OrderResponseDto();
        response.setId(orderId);
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setItems(orderItemsArray);

        return response;
    }

    @Override
    public List<OrderResponseDto> findReceivedOrders() {
        List<Order> orderList = orderRepository.findReceivedOrders();
        return orderList.stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setId(order.getId());
            dto.setOrderNumber(order.getOrderNumber());
            dto.setStatus(order.getStatus());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setPaymentStatus(order.getPaymentStatus());
            dto.setItems(order.getItems());
            return dto;
        }).toList();
    }

    @Override
    public Boolean updateStatus(Long orderId, String status) {
        //validate status
        List<String> validStatus = List.of("RECEIVED", "PREPARING", "READY", "COMPLETED", "CANCELLED");
        if (!validStatus.contains(status)){
            throw new RuntimeException("Invalid status: " + status);
        }
        //update
        boolean updated = orderRepository.updateStatus(orderId, status);
        //order not found
        if (!updated) {
            throw new RuntimeException("Order not found: " + orderId);
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
