package edu.icet.ecom.controller;

import edu.icet.ecom.dto.OrderCreateRequest;
import edu.icet.ecom.dto.OrderResponse;
import edu.icet.ecom.dto.OrderStatusUpdateRequest;
import edu.icet.ecom.dto.OrderWithItemNameResponse;
import edu.icet.ecom.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderResponse>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/find-open-orders")
    public ResponseEntity<List<OrderResponse>> findOpenOrders(){
        return ResponseEntity.ok(orderService.findByStatus("open"));
    }

    @GetMapping("/find-by-status/{status}")
    public ResponseEntity<List<OrderResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    @PutMapping("/update/{orderId}/status")
    ResponseEntity<Object> updateStatus(@PathVariable Integer orderId, @Valid @RequestBody OrderStatusUpdateRequest request){
        orderService.updateStatus(orderId, request.getStatus());
        return ResponseEntity.ok("Order status updated successfully");
    }

    @PutMapping("/{id}/type")
    public ResponseEntity<String> updateOrderType(@PathVariable Integer id, @RequestParam String type) {
        orderService.updateType(id, type);
        return ResponseEntity.ok("Order type successfully updated to: " + type);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/find-all-with-item-names")
    public ResponseEntity<List<OrderWithItemNameResponse>> getAllOrdersWithItemNames() {
        return ResponseEntity.ok(orderService.getAllOrdersWithItemNames());
    }
}
