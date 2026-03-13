package edu.icet.ecom.controller;

import edu.icet.ecom.entity.OrderAssigment;
import edu.icet.ecom.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WaiterController {

    private final WaiterService waiterService;

    @GetMapping("/unserved")
    public List<OrderAssigment> getUnservedOrders(){
        return waiterService.getUnservedOrders();
    }

    @PutMapping("/serve/{orderId}")
    public boolean serveOrder(@PathVariable Long orderId){
        return waiterService.serveOrder(orderId);
    }
}
