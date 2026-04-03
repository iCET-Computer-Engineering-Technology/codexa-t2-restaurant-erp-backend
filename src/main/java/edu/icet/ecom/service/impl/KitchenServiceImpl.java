package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.InventoryDeductionResponse;
import edu.icet.ecom.entity.KitchenOrder;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.entity.WaiterDetails;
import edu.icet.ecom.repository.KitchenOrderRepository;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.InventoryService;
import edu.icet.ecom.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private static final Logger log = LoggerFactory.getLogger(KitchenServiceImpl.class);

    private static final String ORDER_TYPE_DINE_IN = "dine_in";
    private static final String ORDER_TYPE_TAKEOUT = "takeout";
    private static final String ORDER_TYPE_BOOKING = "booking";

    private static final Set<String> VALID_ORDER_TYPES = Set.of(ORDER_TYPE_DINE_IN, ORDER_TYPE_TAKEOUT, ORDER_TYPE_BOOKING);
    private static final Set<String> VALID_ORDER_ITEM_STATUSES = Set.of("pending", "in_progress", "preparing", "fired", "ready", "served", "voided");

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryService inventoryService;

    @Override
    public List<KitchenOrder> getKitchenOrders() {
        return kitchenOrderRepository.getKitchenOrders();
    }

    @Override
    public List<Waiter> getActiveWaiters() {
        return waiterRepository.findActiveWaiters();
    }

    @Override
    public void assignWaiter(Long kitchenOrderId, Long waiterId) {
        KitchenOrder ko = kitchenOrderRepository.findById(kitchenOrderId);
        if (ko == null) {
            throw new IllegalArgumentException("Kitchen order not found");
        }

        if (!"done".equals(ko.getStatus())) {
            throw new IllegalArgumentException("Order not ready for assignment");
        }

        boolean assigned = orderAssignmentRepository.existsByKitchenOrderId(kitchenOrderId);
        if (assigned) {
            throw new IllegalArgumentException("Order already assigned to a waiter");
        }

        orderAssignmentRepository.assignWaiter(kitchenOrderId, waiterId);
    }

    @Override
    public List<WaiterDetails> getAssignmentsWaiter() {
        return orderAssignmentRepository.getAssignments();
    }

    @Override
    public List<Order> getOpenOrders() {
        List<Order> orders = orderRepository.findByStatus("open");
        orders.forEach(order -> order.setItems(orderItemRepository.findByOrderId(order.getId())));
        return orders;
    }

    @Override
    public void sendToKitchen(Long orderId) {
        Order order = orderRepository.findById(orderId.intValue());
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        String normalizedType = normalize(order.getOrderType());
        if (!VALID_ORDER_TYPES.contains(normalizedType)) {
            throw new IllegalArgumentException("Unsupported order type for kitchen routing: " + order.getOrderType());
        }

        boolean exists = kitchenOrderRepository.existsByOrderId(orderId);
        if (exists) {
            throw new IllegalArgumentException("Order already sent to kitchen");
        }

        orderRepository.updateStatus(orderId.intValue(), "sent_to_kitchen");
        kitchenOrderRepository.createKitchenOrder(orderId);
        log.info("{}", buildLabel(order, "KITCHEN_RECEIVED", normalizedType));
    }

    @Override
    public void markOrderReady(Long orderId) {
        KitchenOrder ko = kitchenOrderRepository.findByOrderId(orderId);
        if (ko == null) {
            throw new IllegalArgumentException("Kitchen order not found");
        }

        if ("done".equals(ko.getStatus())) {
            throw new IllegalArgumentException("Order already marked as ready");
        }

        kitchenOrderRepository.markAsDone(orderId);
        orderRepository.updateStatus(Math.toIntExact(orderId), "partially_ready");

        Order order = orderRepository.findById(orderId.intValue());
        if (order != null) {
            log.info("{}", buildLabel(order, "READY_FOR_FULFILLMENT", normalize(order.getOrderType())));
        }
    }

    @Override
    public InventoryDeductionResponse updateOrderItemStatus(Integer orderItemId, String status) {
        if (orderItemId == null || orderItemId <= 0) {
            throw new IllegalArgumentException("Invalid orderItemId");
        }

        String normalizedStatus = normalize(status);
        if (!VALID_ORDER_ITEM_STATUSES.contains(normalizedStatus)) {
            throw new IllegalArgumentException("Unsupported order item status: " + status);
        }

        if (isDeductionTriggerStatus(normalizedStatus)) {
            boolean updated = orderItemRepository.updateStatus(orderItemId, "fired");
            if (!updated) {
                throw new IllegalArgumentException("Order item not found");
            }
            return inventoryService.handleFiredStatus(orderItemId);
        }

        boolean updated = orderItemRepository.updateStatus(orderItemId, normalizedStatus);
        if (!updated) {
            throw new IllegalArgumentException("Order item not found");
        }

        return new InventoryDeductionResponse(false, "Order item status updated");
    }

    private boolean isDeductionTriggerStatus(String status) {
        return "fired".equals(status) || "preparing".equals(status) || "in_progress".equals(status);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String buildLabel(Order order, String stage, String type) {
        String route = switch (type) {
            case ORDER_TYPE_DINE_IN -> "KDS_DINE_IN";
            case ORDER_TYPE_TAKEOUT -> "KDS_TAKEOUT";
            case ORDER_TYPE_BOOKING -> "KDS_BOOKING";
            default -> "KDS_UNKNOWN";
        };

        String destination = switch (type) {
            case ORDER_TYPE_DINE_IN, ORDER_TYPE_BOOKING -> "TABLE-" + (order.getTableId() == null ? "UNASSIGNED" : order.getTableId());
            case ORDER_TYPE_TAKEOUT -> "TAKEOUT-PICKUP";
            default -> "UNASSIGNED";
        };

        return "[" + stage + "] order=" + order.getId()
                + " orderNumber=" + order.getOrderNumber()
                + " type=" + type
                + " route=" + route
                + " destination=" + destination
                + " customerId=" + (order.getCustomerId() == null ? "N/A" : order.getCustomerId());
    }
}

