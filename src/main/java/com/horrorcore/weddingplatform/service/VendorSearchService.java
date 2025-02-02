package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.dto.VendorSearchCriteria;
import com.horrorcore.weddingplatform.model.*;
import com.horrorcore.weddingplatform.repository.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorSearchService {
    private final VendorRepository vendorRepository;
    private final PhotographerRepository photographerRepository;
    private final VenueRepository venueRepository;
    private final CatererRepository catererRepository;
    private final DecoratorRepository decoratorRepository;
    private final TravelPartnerRepository travelPartnerRepository;

    public VendorSearchService(
            VendorRepository vendorRepository,
            PhotographerRepository photographerRepository,
            VenueRepository venueRepository,
            CatererRepository catererRepository,
            DecoratorRepository decoratorRepository,
            TravelPartnerRepository travelPartnerRepository) {
        this.vendorRepository = vendorRepository;
        this.photographerRepository = photographerRepository;
        this.venueRepository = venueRepository;
        this.catererRepository = catererRepository;
        this.decoratorRepository = decoratorRepository;
        this.travelPartnerRepository = travelPartnerRepository;
    }

    public Page<Vendor> searchVendors(VendorSearchCriteria criteria, Pageable pageable) {
        return vendorRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getVendorType() != null) {
                predicates.add(cb.equal(root.get("businessType"), criteria.getVendorType()));
            }

            if (criteria.getVerified() != null) {
                predicates.add(cb.equal(root.get("verified"), criteria.getVerified()));
            }

            if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
                String searchPattern = "%" + criteria.getSearchTerm().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("businessName")), searchPattern),
                        cb.like(cb.lower(root.get("description")), searchPattern)
                ));
            }

            if (criteria.getLocations() != null && !criteria.getLocations().isEmpty()) {
                predicates.add(root.get("registeredAddress").in(criteria.getLocations()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public Page<PhotographerDetails> searchPhotographers(VendorSearchCriteria criteria, Pageable pageable) {
        return photographerRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getDronePhotography() != null) {
                predicates.add(cb.equal(root.get("dronePhotographyAvailable"), criteria.getDronePhotography()));
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("basePrice"), criteria.getMaxPrice()));
            }

            // Add vendor-related criteria
            if (criteria.getVerified() != null || !criteria.getSearchTerm().isEmpty()) {
                Join<PhotographerDetails, Vendor> vendorJoin = root.join("vendor");

                if (criteria.getVerified() != null) {
                    predicates.add(cb.equal(vendorJoin.get("verified"), criteria.getVerified()));
                }

                if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
                    String searchPattern = "%" + criteria.getSearchTerm().toLowerCase() + "%";
                    predicates.add(cb.or(
                            cb.like(cb.lower(vendorJoin.get("businessName")), searchPattern),
                            cb.like(cb.lower(root.get("description")), searchPattern)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public Page<CatererDetails> searchCaterers(VendorSearchCriteria criteria, Pageable pageable) {
        return catererRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Handle cuisine types
            if (criteria.getCuisineTypes() != null && !criteria.getCuisineTypes().isEmpty()) {
                for (String cuisine : criteria.getCuisineTypes()) {
                    predicates.add(cb.isMember(cuisine, root.get("cuisineTypes")));
                }
            }

            // Handle price range
            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("perPlateStartingCost"), criteria.getMaxPrice()));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("perPlateStartingCost"), criteria.getMinPrice()));
            }

            // Add vendor-related criteria
            if (criteria.getVerified() != null || (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty())) {
                Join<CatererDetails, Vendor> vendorJoin = root.join("vendor");

                if (criteria.getVerified() != null) {
                    predicates.add(cb.equal(vendorJoin.get("verified"), criteria.getVerified()));
                }

                if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
                    String searchPattern = "%" + criteria.getSearchTerm().toLowerCase() + "%";
                    predicates.add(cb.or(
                            cb.like(cb.lower(vendorJoin.get("businessName")), searchPattern),
                            cb.like(cb.lower(root.get("description")), searchPattern)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public Page<VenueDetails> searchVenues(VendorSearchCriteria criteria, Pageable pageable) {
        return venueRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getMinimumCapacity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxCapacity"), criteria.getMinimumCapacity()));
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("costPerPlate"), criteria.getMaxPrice()));
            }

            // Add vendor-related criteria
            if (criteria.getVerified() != null || (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty())) {
                Join<VenueDetails, Vendor> vendorJoin = root.join("vendor");

                if (criteria.getVerified() != null) {
                    predicates.add(cb.equal(vendorJoin.get("verified"), criteria.getVerified()));
                }

                if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
                    String searchPattern = "%" + criteria.getSearchTerm().toLowerCase() + "%";
                    predicates.add(cb.or(
                            cb.like(cb.lower(vendorJoin.get("businessName")), searchPattern),
                            cb.like(cb.lower(root.get("description")), searchPattern)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}