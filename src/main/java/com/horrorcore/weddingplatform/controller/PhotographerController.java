package com.horrorcore.weddingplatform.controller;

import com.horrorcore.weddingplatform.model.PhotographerDetails;
import com.horrorcore.weddingplatform.service.PhotographerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendors/photographers")
public class PhotographerController {
    private final PhotographerService photographerService;

    public PhotographerController(PhotographerService photographerService) {
        this.photographerService = photographerService;
    }

    @PostMapping("/{vendorId}")
    public ResponseEntity<PhotographerDetails> registerPhotographerDetails(
            @PathVariable Long vendorId,
            @Valid @RequestBody PhotographerDetails details) {
        return ResponseEntity.ok(photographerService.registerPhotographer(vendorId, details));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotographerDetails> getPhotographerDetails(@PathVariable Long id) {
        // Implementation here
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotographerDetails> updatePhotographerDetails(
            @PathVariable Long id,
            @Valid @RequestBody PhotographerDetails details) {
        // Implementation here
        return ResponseEntity.ok().build();
    }
}