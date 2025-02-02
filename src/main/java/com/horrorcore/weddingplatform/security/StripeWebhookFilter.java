package com.horrorcore.weddingplatform.security;

import com.stripe.net.Webhook;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class StripeWebhookFilter extends OncePerRequestFilter {
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/api/webhooks/stripe")) {
            String signature = request.getHeader("Stripe-Signature");

            if (signature == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Store request body for later verification
            String payload = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            try {
                // Verify signature
                Webhook.Signature.verifyHeader(
                        payload,
                        signature,
                        webhookSecret,
                        300L // 5 minute tolerance
                );
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}