package com.horrorcore.weddingplatform.service;


import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorVerificationService {
    private final VendorRepository vendorRepository;

    public VendorVerificationService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public Vendor registerVendor(Vendor vendor) {
        // Perform initial validation
        if (vendorRepository.existsByEmail(vendor.getEmail())) {
            throw new RuntimeException("Vendor with this email already exists");
        }
        if (vendorRepository.existsByBusinessName(vendor.getBusinessName())) {
            throw new RuntimeException("Vendor with this business name already exists");
        }

        // Set initial verification status
        vendor.setVerified(false);

        return vendorRepository.save(vendor);
    }

    @Transactional
    public Vendor verifyVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Implement verification logic here
        // This could include document verification, background checks, etc.
        vendor.setVerified(true);

        return vendorRepository.save(vendor);
    }
}