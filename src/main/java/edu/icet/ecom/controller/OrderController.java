package edu.icet.ecom.controller;

import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import edu.icet.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @GetMapping("/open-orders")
    public ResponseEntity<List<OrderResponseDto>> findOpenOrders(){
        return  ResponseEntity.ok(orderService.findOpenOrders());
    }

    @PutMapping("/update/{orderId}/status")
    ResponseEntity<String> updateStatus(@PathVariable Integer orderId, @RequestParam String status){
        boolean updated = orderService.updateStatus(orderId, status);
        if(updated){
            return ResponseEntity.ok("Order status updated successfully");
        }else{
            return ResponseEntity.badRequest().body("Update failed");
        }
    }
}
