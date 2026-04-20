package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> addPayment(@RequestBody PaymentDto paymentDto){
        try {
            boolean result = paymentService.addPayment(paymentDto);

            if (result) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Payment Processed Successfully");
                response.put("orderId", paymentDto.getOrderId());
                return ResponseEntity.ok(response);
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Payment Failed");
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (IllegalArgumentException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Something Went Wrong: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

        @GetMapping
        public ResponseEntity<?> getAll() {
            try {
                List<PaymentDto> payments = paymentService.getAllPayments();

                if (payments.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }

                return ResponseEntity.ok(payments);

            } catch (Exception e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Failed to fetch payments: " + e.getMessage());
                return ResponseEntity.internalServerError().body(errorResponse);
            }
        }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Integer orderId){
        try{
            PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Something went wrong: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
