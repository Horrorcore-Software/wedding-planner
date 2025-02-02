package com.horrorcore.weddingplatform.controller;


import com.horrorcore.weddingplatform.dto.VendorSearchCriteria;
import com.horrorcore.weddingplatform.model.CatererDetails;
import com.horrorcore.weddingplatform.model.PhotographerDetails;
import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.model.VenueDetails;
import com.horrorcore.weddingplatform.service.VendorSearchService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class VendorSearchController {
    private final VendorSearchService searchService;

    public VendorSearchController(VendorSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/vendors")
    public ResponseEntity<Page<Vendor>> searchVendors(
            @Valid VendorSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchVendors(criteria, pageable));
    }

    @GetMapping("/photographers")
    public ResponseEntity<Page<PhotographerDetails>> searchPhotographers(
            @Valid VendorSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchPhotographers(criteria, pageable));
    }

    @GetMapping("/venues")
    public ResponseEntity<Page<VenueDetails>> searchVenues(
            @Valid VendorSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchVenues(criteria, pageable));
    }

    @GetMapping("/caterers")
    public ResponseEntity<Page<CatererDetails>> searchCaterers(
            @Valid VendorSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchCaterers(criteria, pageable));
    }
}