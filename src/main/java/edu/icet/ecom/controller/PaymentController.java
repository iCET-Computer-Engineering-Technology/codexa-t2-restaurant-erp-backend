package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/payments" ,"/api/payments"})
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> addPayment(@RequestBody PaymentDto paymentDto){
        try {
            boolean result = paymentService.addPayment(paymentDto);

            if (result) {
                return ResponseEntity.ok("Payment Processed Successfully");
            }
            return ResponseEntity.badRequest().body("payment Failed");

        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Something Went Wrong :" + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        try{
            List<PaymentDto> payments = paymentService.getAllPayment();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to fetch payments: " + e.getMessage());
        }
    }

    @GetMapping("/order/{orderid}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Integer orderId){
        try{
            PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
           return ResponseEntity.internalServerError()
                   .body("Something went wrong : " + e.getMessage());
        }
    }
}
