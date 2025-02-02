package com.horrorcore.weddingplatform.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "venue_details")
public class VenueDetails extends VendorSpecialization {
    @Min(1)
    private Integer maxCapacity;

    @NotNull
    private Double costPerPlate;

    @ElementCollection
    private List<String> amenities;

    @ElementCollection
    private List<String> restrictions;

    private boolean outdoorSpaceAvailable;
    private boolean parkingAvailable;
    private Integer parkingCapacity;

    @NotBlank
    private String cancellationPolicy;

    @ElementCollection
    private List<String> preferredVendors;
}
