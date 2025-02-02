package com.horrorcore.weddingplatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @NotNull
    private LocalDateTime paymentDate;

    private String transactionId;

    @Column(length = 1000)
    private String paymentDetails;

    private LocalDateTime processedDate;
}