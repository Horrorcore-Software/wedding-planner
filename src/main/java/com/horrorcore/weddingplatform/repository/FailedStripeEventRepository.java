package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.FailedStripeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface FailedStripeEventRepository extends JpaRepository<FailedStripeEvent, Long> {
    List<FailedStripeEvent> findByStatusAndNextRetryTimeBefore(
            FailedStripeEvent.FailedEventStatus status,
            LocalDateTime time
    );

    @Query("SELECT f FROM FailedStripeEvent f WHERE f.status = 'PENDING' AND f.retryCount < 3")
    List<FailedStripeEvent> findRetryableEvents();
}