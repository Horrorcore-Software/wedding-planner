package com.horrorcore.weddingplatform.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StripePaymentRequest {
    private Long bookingId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethodId;
}
