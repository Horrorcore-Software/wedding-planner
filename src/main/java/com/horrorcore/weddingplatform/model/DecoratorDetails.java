package com.horrorcore.weddingplatform.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "decorator_details")
public class DecoratorDetails extends VendorSpecialization {
    @ElementCollection
    @NotEmpty(message = "At least one decoration style must be specified")
    private List<String> decorationStyles;

    @ElementCollection
    private List<String> specializations; // e.g., "Floral", "Lighting", "Draping"

    private boolean providesCustomDesigns;
    private boolean providesSetupAndTeardown;

    @Min(1)
    private Integer setupTimeInHours;

    @ElementCollection
    private List<String> materialTypes; // e.g., "Fresh Flowers", "Artificial Flowers", "Fabrics"

    @NotBlank
    private String cancellationPolicy;

    private boolean providesEmergencyService;
}