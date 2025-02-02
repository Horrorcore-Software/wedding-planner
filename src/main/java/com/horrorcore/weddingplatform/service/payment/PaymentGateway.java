package com.horrorcore.weddingplatform.service.payment;


import com.horrorcore.weddingplatform.model.Payment;
import java.math.BigDecimal;

public interface PaymentGateway {
    String initiatePayment(Payment payment);
    Payment confirmPayment(String paymentIntentId);
    void processRefund(Payment payment);
    void handleWebhookEvent(String payload, String signatureHeader);
}
