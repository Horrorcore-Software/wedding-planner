package com.horrorcore.weddingplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "travel_partner_details")
public class TravelPartnerDetails extends VendorSpecialization {
    // Fleet management details
    @ElementCollection
    @NotEmpty(message = "At least one vehicle type must be specified")
    private List<String> fleetTypes; // e.g., "Luxury Sedan", "SUV", "Minivan"

    @Min(1)
    private Integer totalFleetSize;

    @ElementCollection
    private List<String> serviceAreas; // Coverage areas for the service

    private boolean providesChauffeur;
    private boolean providesInternationalService;

    @Min(1)
    private Integer advanceBookingDays;

    @NotBlank
    private String insuranceDetails;
}
