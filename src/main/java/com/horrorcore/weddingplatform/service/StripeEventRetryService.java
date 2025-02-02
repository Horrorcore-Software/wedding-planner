package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.model.FailedStripeEvent;
import com.horrorcore.weddingplatform.repository.FailedStripeEventRepository;
import com.horrorcore.weddingplatform.service.payment.EnhancedStripePaymentGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StripeEventRetryService {
    private final FailedStripeEventRepository failedEventRepository;
    private final EnhancedStripePaymentGateway stripePaymentGateway;

    public StripeEventRetryService(
            FailedStripeEventRepository failedEventRepository,
            EnhancedStripePaymentGateway stripePaymentGateway) {
        this.failedEventRepository = failedEventRepository;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    @Scheduled(fixedRate = 900000) // Run every 15 minutes
    public void retryFailedEvents() {
        List<FailedStripeEvent> retryableEvents = failedEventRepository.findRetryableEvents();

        for (FailedStripeEvent event : retryableEvents) {
            try {
                event.setStatus(FailedStripeEvent.FailedEventStatus.RETRYING);
                failedEventRepository.save(event);

                // Retry processing the event
                stripePaymentGateway.confirmPayment(Event.fromJson(event.getPayload()));

                // If successful, mark as resolved
                event.setStatus(FailedStripeEvent.FailedEventStatus.RESOLVED);
                failedEventRepository.save(event);
            } catch (Exception e) {
                // Handle retry failure
                event.setRetryCount(event.getRetryCount() + 1);
                event.setStatus(FailedStripeEvent.FailedEventStatus.PENDING);
                event.setErrorMessage("Retry failed: " + e.getMessage());

                // Calculate next retry time with exponential backoff
                int delayMinutes = (int) Math.pow(2, event.getRetryCount());
                event.setNextRetryTime(LocalDateTime.now().plusMinutes(delayMinutes));

                if (event.getRetryCount() >= 3) {
                    event.setStatus(FailedStripeEvent.FailedEventStatus.FAILED);
                }

                failedEventRepository.save(event);
            }
        }
    }
}