package com.horrorcore.weddingplatform.controller;


import com.horrorcore.weddingplatform.model.TravelPartnerDetails;
import com.horrorcore.weddingplatform.service.TravelPartnerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors/travel-partners")
public class TravelPartnerController {
    private final TravelPartnerService travelPartnerService;

    public TravelPartnerController(TravelPartnerService travelPartnerService) {
        this.travelPartnerService = travelPartnerService;
    }

    @PostMapping("/{vendorId}")
    public ResponseEntity<TravelPartnerDetails> registerTravelPartnerDetails(
            @PathVariable Long vendorId,
            @Valid @RequestBody TravelPartnerDetails details) {
        return ResponseEntity.ok(travelPartnerService.registerTravelPartner(vendorId, details));
    }

    @GetMapping("/search/by-area/{area}")
    public ResponseEntity<List<TravelPartnerDetails>> findByServiceArea(@PathVariable String area) {
        return ResponseEntity.ok(travelPartnerService.findByServiceArea(area));
    }

    @GetMapping("/search/by-fleet-type/{fleetType}")
    public ResponseEntity<List<TravelPartnerDetails>> findByFleetType(@PathVariable String fleetType) {
        return ResponseEntity.ok(travelPartnerService.findByFleetType(fleetType));
    }
}
