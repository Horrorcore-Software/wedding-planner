package com.horrorcore.weddingplatform.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "photographer_details")
public class PhotographerDetails extends VendorSpecialization {
    @ElementCollection
    private List<String> equipmentList;

    @ElementCollection
    @NotEmpty(message = "At least one photography package must be specified")
    private List<String> photographyPackages;

    private boolean dronePhotographyAvailable;
    private boolean videoServicesAvailable;

    @Min(1)
    private Integer minimumBookingHours;
}
