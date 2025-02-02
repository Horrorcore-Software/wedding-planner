package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.dto.StripePaymentResponse;
import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.*;
import com.horrorcore.weddingplatform.repository.*;
import com.horrorcore.weddingplatform.service.payment.PaymentGateway;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            @Qualifier("enhancedStripePaymentGateway") PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public Payment processPayment(@Valid PaymentRequest paymentRequest) {
        // Retrieve booking
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new VendorException("Booking not found", HttpStatus.NOT_FOUND));

        // Validate payment amount and type
        validatePayment(booking, paymentRequest);

        // Create payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(paymentRequest.getAmount());
        payment.setType(paymentRequest.getType());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(generateTransactionId());

        // Process payment with payment gateway
        try {
            processPaymentWithGateway(paymentRequest);
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setProcessedDate(LocalDateTime.now());
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaymentDetails("Payment failed: " + e.getMessage());
            throw new VendorException("Payment processing failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Update booking status if payment is successful
        updateBookingStatus(booking, payment);

        return paymentRepository.save(payment);
    }

    private void validatePayment(Booking booking, PaymentRequest paymentRequest) {
        // Validate payment amount based on payment type
        switch (paymentRequest.getType()) {
            case ADVANCE_PAYMENT:
                if (paymentRequest.getAmount().compareTo(booking.getAdvancePayment()) != 0) {
                    throw new VendorException(
                            "Invalid advance payment amount. Expected: " + booking.getAdvancePayment(),
                            HttpStatus.BAD_REQUEST
                    );
                }
                break;
            case FULL_PAYMENT:
                if (paymentRequest.getAmount().compareTo(booking.getTotalAmount()) != 0) {
                    throw new VendorException(
                            "Invalid full payment amount. Expected: " + booking.getTotalAmount(),
                            HttpStatus.BAD_REQUEST
                    );
                }
                break;
            case INSTALLMENT:
                validateInstallmentPayment(booking, paymentRequest.getAmount());
                break;
        }
    }

    private void validateInstallmentPayment(Booking booking, BigDecimal amount) {
        // Calculate remaining amount
        BigDecimal paidAmount = calculatePaidAmount(booking.getId());
        BigDecimal remainingAmount = booking.getTotalAmount().subtract(paidAmount);

        if (amount.compareTo(remainingAmount) > 0) {
            throw new VendorException(
                    "Payment amount exceeds remaining balance: " + remainingAmount,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private BigDecimal calculatePaidAmount(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void processPaymentWithGateway(PaymentRequest paymentRequest) {
        // This is where you would integrate with a payment gateway
        // For now, we'll simulate the process
        if (paymentRequest.getCardNumber() != null &&
                paymentRequest.getCardNumber().startsWith("4")) {
            throw new VendorException("Payment declined", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public StripePaymentResponse initiatePayment(@Valid PaymentRequest paymentRequest) {
        // Validate booking and payment details
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new VendorException("Booking not found", HttpStatus.NOT_FOUND));

        validatePayment(booking, paymentRequest);

        // Create payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(paymentRequest.getAmount());
        payment.setType(paymentRequest.getType());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        // Initiate payment with Stripe
        try {
            String clientSecret = paymentGateway.initiatePayment(payment);

            StripePaymentResponse response = new StripePaymentResponse();
            response.setClientSecret(clientSecret);
            response.setStatus("PENDING");
            return response;
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaymentDetails("Payment initiation failed: " + e.getMessage());
            paymentRepository.save(payment);
            throw new VendorException("Payment initiation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Payment confirmPayment(String paymentIntentId) {
        try {
            Payment payment = paymentGateway.confirmPayment(paymentIntentId);
    
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setProcessedDate(LocalDateTime.now());
            
            payment = paymentRepository.save(payment);
    
            updateBookingStatus(payment.getBooking(), payment);
    
            return payment;
        } catch (Exception e) {
            throw new VendorException("Payment confirmation failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void updateBookingStatus(Booking booking, Payment payment) {
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            BigDecimal totalPaid = calculatePaidAmount(booking.getId());

            if (totalPaid.compareTo(booking.getTotalAmount()) >= 0) {
                booking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }
        }
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Transactional
    public Payment processRefund(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new VendorException("Payment not found", HttpStatus.NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new VendorException(
                    "Cannot refund payment with status: " + payment.getStatus(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Create refund record
        Payment refund = new Payment();
        refund.setBooking(payment.getBooking());
        refund.setAmount(payment.getAmount());
        refund.setType(PaymentType.REFUND);
        refund.setStatus(PaymentStatus.PROCESSING);
        refund.setPaymentDate(LocalDateTime.now());
        refund.setTransactionId(generateTransactionId());

        // Process refund with payment gateway
        try {
            processRefundWithGateway(payment);
            refund.setStatus(PaymentStatus.COMPLETED);
            refund.setProcessedDate(LocalDateTime.now());

            // Update original payment status
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        } catch (Exception e) {
            refund.setStatus(PaymentStatus.FAILED);
            refund.setPaymentDetails("Refund failed: " + e.getMessage());
            throw new VendorException("Refund processing failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return paymentRepository.save(refund);
    }

    private void processRefundWithGateway(Payment payment) {
        // Integrate with payment gateway for refund processing
        // For now, we'll simulate the process
    }

    public List<Payment> getBookingPayments(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new VendorException("Booking not found", HttpStatus.NOT_FOUND);
        }
        return paymentRepository.findByBookingId(bookingId);
    }

    public void handleStripeWebhook(String payload, String signatureHeader) {
        paymentGateway.handleWebhookEvent(payload, signatureHeader);
    }
}
