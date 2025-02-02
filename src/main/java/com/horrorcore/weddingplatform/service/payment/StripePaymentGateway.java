package com.horrorcore.weddingplatform.service.payment;


import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.Payment;
import com.horrorcore.weddingplatform.model.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    public String initiatePayment(Payment payment) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // Convert to cents
                    .setCurrency("usd")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("bookingId", payment.getBooking().getId().toString())
                    .putMetadata("paymentId", payment.getId().toString())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            throw new VendorException("Failed to initiate payment: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Payment confirmPayment(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent confirmedPayment = paymentIntent.confirm();
            
            // Create and return a Payment object based on the confirmed PaymentIntent
            Payment payment = new Payment();
            payment.setTransactionId(confirmedPayment.getId());
            payment.setAmount(BigDecimal.valueOf(confirmedPayment.getAmount()).divide(BigDecimal.valueOf(100)));
            payment.setStatus(PaymentStatus.COMPLETED);
            
            return payment;
        } catch (StripeException e) {
            throw new VendorException("Failed to confirm payment: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void processRefund(Payment payment) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(payment.getTransactionId())
                    .setAmount(payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                    .putMetadata("bookingId", payment.getBooking().getId().toString())
                    .putMetadata("paymentId", payment.getId().toString())
                    .build();

            Refund.create(params);
        } catch (StripeException e) {
            throw new VendorException("Failed to process refund: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handleWebhookEvent(String payload, String signatureHeader) {
        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            // Handle different event types
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed(event);
                    break;
                case "charge.refunded":
                    handleRefundSucceeded(event);
                    break;
                default:
                    // Log unhandled event type
                    break;
            }
        } catch (Exception e) {
            throw new VendorException("Failed to process webhook: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void handlePaymentIntentSucceeded(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

        // Update payment status to COMPLETED
        String paymentId = paymentIntent.getMetadata().get("paymentId");
        // Implement payment status update logic
    }

    private void handlePaymentIntentFailed(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

        // Update payment status to FAILED
        String paymentId = paymentIntent.getMetadata().get("paymentId");
        // Implement payment failure handling logic
    }

    private void handleRefundSucceeded(Event event) {
        // Handle successful refund
        // Update payment and booking status accordingly
    }
}