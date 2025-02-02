package com.horrorcore.weddingplatform.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.public.key}")
    private String publicKey;

    @PostConstruct
    public void init() {
        // Initialize Stripe API with your secret key
        Stripe.apiKey = apiKey;
    }

    // Make keys available to the application
    @Bean
    public String stripePublicKey() {
        return publicKey;
    }

    @Bean
    public String stripeWebhookSecret() {
        return webhookSecret;
    }
}