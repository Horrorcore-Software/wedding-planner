package com.horrorcore.weddingplatform.controller;


import com.horrorcore.weddingplatform.model.Payment;
import com.horrorcore.weddingplatform.model.PaymentRequest;
import com.horrorcore.weddingplatform.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.processPayment(paymentRequest));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> processRefund(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.processRefund(paymentId));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<List<Payment>> getBookingPayments(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getBookingPayments(bookingId));
    }
}