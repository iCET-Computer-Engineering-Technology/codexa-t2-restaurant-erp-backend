package edu.icet.ecom.controller;

import edu.icet.ecom.dto.OrderTypeDto;
import edu.icet.ecom.service.OrderTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-types")
@RequiredArgsConstructor
@CrossOrigin
public class OrderTypeController {

    private final OrderTypeService orderTypeService;

    @GetMapping
    public ResponseEntity<List<OrderTypeDto>> getAllOrderTypes() {
        List<OrderTypeDto> orderTypes = orderTypeService.getAllOrderTypes();
        return ResponseEntity.ok(orderTypes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderTypeDto>> getActiveOrderTypes() {
        List<OrderTypeDto> orderTypes = orderTypeService.getActiveOrderTypes();
        return ResponseEntity.ok(orderTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTypeDto> getOrderTypeById(@PathVariable Integer id) {
        OrderTypeDto orderType = orderTypeService.getOrderTypeById(id);
        if (orderType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderType);
    }

    @GetMapping("/by-name/{typeName}")
    public ResponseEntity<OrderTypeDto> getOrderTypeByName(@PathVariable String typeName) {
        OrderTypeDto orderType = orderTypeService.getOrderTypeByName(typeName);
        if (orderType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderType);
    }
}

