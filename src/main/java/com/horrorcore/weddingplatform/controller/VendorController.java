package com.horrorcore.weddingplatform.controller;

import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.service.VendorVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {
    private final VendorVerificationService vendorVerificationService;

    public VendorController(VendorVerificationService vendorVerificationService) {
        this.vendorVerificationService = vendorVerificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Vendor> registerVendor(@Valid @RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorVerificationService.registerVendor(vendor));
    }

    @PostMapping("/{vendorId}/verify")
    public ResponseEntity<Vendor> verifyVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(vendorVerificationService.verifyVendor(vendorId));
    }
}