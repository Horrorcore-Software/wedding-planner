package com.horrorcore.weddingplatform.dto;

import lombok.Data;

@Data
public class StripePaymentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private String status;
}

