package com.horrorcore.weddingplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private LocalDateTime bookingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Min(1)
    private Integer numberOfGuests;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private BigDecimal advancePayment;

    private String specialRequirements;

    @Column(name = "contact_name")
    @NotBlank
    private String contactName;

    @Column(name = "contact_email")
    @NotBlank
    @Email
    private String contactEmail;

    @Column(name = "contact_phone")
    @NotBlank
    private String contactPhone;

    @NotBlank
    private String eventLocation;
}

