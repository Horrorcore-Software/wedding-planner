package com.horrorcore.weddingplatform.repository;


import com.horrorcore.weddingplatform.model.Payment;
import com.horrorcore.weddingplatform.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingId(Long bookingId);
    List<Payment> findByStatus(PaymentStatus status);
    boolean existsByBookingIdAndStatus(Long bookingId, PaymentStatus status);
}