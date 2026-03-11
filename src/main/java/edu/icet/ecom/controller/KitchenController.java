package edu.icet.ecom.controller;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.service.KitchenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return kitchenService.getKitchenOrders();
    }

    @GetMapping("/waiters")
    public List<Waiter> getWaiters(){

        return kitchenService.getActiveWaiters();

    }
}
