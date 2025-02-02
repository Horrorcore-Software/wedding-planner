package com.horrorcore.weddingplatform.dto;


import com.horrorcore.weddingplatform.model.*;
import com.horrorcore.weddingplatform.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.Data;
import java.util.List;

@Data
public class VendorSearchCriteria {
    private VendorType vendorType;
    private Double maxPrice;
    private Double minPrice;
    private List<String> locations;
    private Boolean verified;
    private Integer minRating;
    private String searchTerm;

    // Specialized search criteria
    private List<String> cuisineTypes;  // For caterers
    private List<String> decorationStyles;  // For decorators
    private List<String> fleetTypes;  // For travel partners
    private Integer minimumCapacity;  // For venues
    private Boolean dronePhotography;  // For photographers
}