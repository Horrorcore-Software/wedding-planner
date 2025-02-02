package com.horrorcore.weddingplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "failed_stripe_events")
public class FailedStripeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stripeEventId;

    @Column(nullable = false)
    private String eventType;

    @Column(length = 2000)
    private String payload;

    @Column(length = 1000)
    private String errorMessage;

    private Integer retryCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime nextRetryTime;

    @Enumerated(EnumType.STRING)
    private FailedEventStatus status = FailedEventStatus.PENDING;

    public enum FailedEventStatus {
        PENDING,
        RETRYING,
        RESOLVED,
        FAILED
    }
}
