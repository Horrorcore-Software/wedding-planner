package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.dto.VendorSearchCriteria;
import com.horrorcore.weddingplatform.model.*;
import com.horrorcore.weddingplatform.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;


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
        // Create a dynamic query based on the criteria
        Specification<Vendor> spec = Specification.where(null);

        if (criteria.getVendorType() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("businessType"), criteria.getVendorType()));
        }

        if (criteria.getVerified() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("verified"), criteria.getVerified()));
        }

        if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("businessName")),
                                    "%" + criteria.getSearchTerm().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")),
                                    "%" + criteria.getSearchTerm().toLowerCase() + "%")
                    ));
        }

        // Add location-based filtering
        if (criteria.getLocations() != null && !criteria.getLocations().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.get("registeredAddress").in(criteria.getLocations()));
        }

        return vendorRepository.findAll(spec, pageable);
    }

    // Specialized search methods for each vendor type
    public Page<PhotographerDetails> searchPhotographers(VendorSearchCriteria criteria, Pageable pageable) {
        Specification<PhotographerDetails> spec = Specification.where(null);

        if (criteria.getDronePhotography() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("dronePhotographyAvailable"), criteria.getDronePhotography()));
        }

        if (criteria.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("basePrice"), criteria.getMaxPrice()));
        }

        return photographerRepository.findAll(spec, pageable);
    }

    public Page<VenueDetails> searchVenues(VendorSearchCriteria criteria, Pageable pageable) {
        Specification<VenueDetails> spec = Specification.where(null);

        if (criteria.getMinimumCapacity() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("maxCapacity"), criteria.getMinimumCapacity()));
        }

        return venueRepository.findAll(spec, pageable);
    }

    public Page<CatererDetails> searchCaterers(VendorSearchCriteria criteria, Pageable pageable) {
        Specification<CatererDetails> spec = Specification.where(null);

        if (criteria.getCuisineTypes() != null && !criteria.getCuisineTypes().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.and(criteria.getCuisineTypes().stream()
                            .map(cuisine -> cb.isMember(cuisine, root.get("cuisineTypes")))
                            .toArray(Predicate[]::new)));
        }

        return catererRepository.findAll(spec, pageable);
    }
}