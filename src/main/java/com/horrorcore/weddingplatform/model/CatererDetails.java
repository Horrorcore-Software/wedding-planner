package com.horrorcore.weddingplatform.model;


import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "caterer_details")
public class CatererDetails extends VendorSpecialization {
    @ElementCollection
    @NotEmpty(message = "At least one cuisine type must be specified")
    private List<String> cuisineTypes;

    @Min(1)
    private Integer minimumGuestCount;

    @Min(1)
    private Integer maximumGuestCount;

    private boolean providesBuffetService;
    private boolean providesPlatedService;
    private boolean providesBeverageService;

    @ElementCollection
    private List<String> dietaryAccommodations; // e.g., "Vegetarian", "Vegan", "Gluten-Free"

    @NotNull
    private Double perPlateStartingCost;

    private boolean providesTableware;
    private boolean providesStaffing;
}