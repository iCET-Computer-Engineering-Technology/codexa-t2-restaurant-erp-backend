package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.*;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderItemRepository orderItemRepository;

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
        orders.forEach(order ->
                order.setItems(
                        orderItemRepository.findByOrderId(order.getId())
                )
        );
        return orders;
    }

    @Override
    public void sendToKitchen(Long orderId) {
        Order order = orderRepository.findById(orderId.intValue());
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        boolean exists = kitchenOrderRepository.existsByOrderId(orderId);
        if (exists) {
            throw new IllegalArgumentException("Order already sent to kitchen");
        }

        orderRepository.updateStatus(orderId.intValue(), "sent_to_kitchen");
        kitchenOrderRepository.createKitchenOrder(orderId);
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
        orderRepository.updateStatus(orderId.intValue(), "partially_ready");
    }
}

