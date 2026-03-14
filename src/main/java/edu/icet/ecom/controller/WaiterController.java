package edu.icet.ecom.controller;

import edu.icet.ecom.entity.OrderAssigment;
import edu.icet.ecom.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final WaiterService waiterService;

    @GetMapping("/unserved")
    public List<OrderAssigment> getUnservedOrders(@PathVariable Long waiterId){
        return waiterService.getUnservedOrders(waiterId);
    }

    @PutMapping("/{orderId}")
    public boolean serveOrder(@PathVariable Long assignmentId){
        return waiterService.serveOrder(assignmentId);
    }
}
