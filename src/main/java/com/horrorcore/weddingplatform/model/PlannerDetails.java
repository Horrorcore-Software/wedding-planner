package com.horrorcore.weddingplatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "planner_details")
public class PlannerDetails extends VendorSpecialization {
    @ElementCollection
    @NotEmpty
    private List<String> servicePackages;

    @Min(1)
    private Integer yearsOfExperience;

    @Min(1)
    private Integer maximumEventsPerMonth;

    private boolean destinationWeddingExperience;
    private boolean internationalExperience;

    @ElementCollection
    private List<String> specialties;
}