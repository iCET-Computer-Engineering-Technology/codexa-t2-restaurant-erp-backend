package edu.icet.ecom.controller;

import edu.icet.ecom.entity.OrderAssigment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.codexa.retauranterp.service.WaiterService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin
public class WaiterController {

    private final WaiterService waiterService;

    @GetMapping("/unserved")
    public List<OrderAssigment> getUnservedOrders(){
        return waiterService.getUnservedOrders();
    }

    @PutMapping("/serve/{orderId}")
    public void serveOrder(@PathVariable Long orderId){
        waiterService.serveOrder(orderId);
    }
}
