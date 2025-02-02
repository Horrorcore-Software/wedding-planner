package com.horrorcore.weddingplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.horrorcore.weddingplatform.model.Vendor;

// Base class for vendor specializations
@MappedSuperclass
@Data
public abstract class VendorSpecialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @NotNull
    private Double basePrice;

    @Size(max = 1000)
    private String description;
}