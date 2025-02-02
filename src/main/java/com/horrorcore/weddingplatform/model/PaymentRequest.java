package com.horrorcore.weddingplatform.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    @Min(value = 1)
    private BigDecimal amount;

    @NotNull
    private PaymentType type;

    private String paymentMethod;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String billingAddress;
}