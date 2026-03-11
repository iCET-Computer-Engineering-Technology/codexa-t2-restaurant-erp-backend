package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.service.KitchenService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;

    public KitchenServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getKitchenOrders() {
        return orderRepository.findReceivedOrders();
    }
}
