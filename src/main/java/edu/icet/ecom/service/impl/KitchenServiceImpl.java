package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.KitchenOrder;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.KitchenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderItemRepository orderItemRepository;

    public KitchenServiceImpl(OrderRepository orderRepository, WaiterRepository waiterRepository, OrderAssignmentRepository orderAssignmentRepository, KitchenOrderRepository kitchenOrderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.waiterRepository = waiterRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.kitchenOrderRepository = kitchenOrderRepository;
        this.orderItemRepository = orderItemRepository;
    }

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
        waiterRepository.assignWaiter(kitchenOrderId, waiterId);

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
    public List<OrderAssignment> getAssignments() {
        return orderAssignmentRepository.getAssignments();
    }

    @Override
    public List<Order> getOpenOrders() {
        List<Order> orders = orderRepository.findOpenOrders();

        orders.forEach(order ->
                order.setItems(
                        orderItemRepository.findByOrderId(order.getId())
                )
        );

        return orders;
    }

    @Override
    public void sendToKitchen(Long orderId) {

        boolean exists = kitchenOrderRepository.existsByOrderId(orderId);
        if (exists) {
            throw new IllegalArgumentException("Order already sent to kitchen");
        }
        kitchenOrderRepository.createKitchenOrder(orderId);
        orderRepository.updateStatus(orderId, "sent_to_kitchen");
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
        orderRepository.updateStatus(orderId, "partially_ready");
    }
}
