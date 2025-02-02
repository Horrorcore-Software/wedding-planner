package com.horrorcore.weddingplatform.service;


import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.repository.VendorRepository;
import org.springframework.http.HttpStatus;
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
        if (vendorRepository.existsByEmail(vendor.getEmail())) {
            throw new VendorException(
                    "A vendor with email " + vendor.getEmail() + " already exists",
                    HttpStatus.CONFLICT
            );
        }

        // Validate business name uniqueness
        if (vendorRepository.existsByBusinessName(vendor.getBusinessName())) {
            throw new VendorException(
                    "A vendor with business name " + vendor.getBusinessName() + " already exists",
                    HttpStatus.CONFLICT
            );
        }

        // Validate required documents
        if (vendor.getVerificationDocuments() == null) {
            throw new VendorException(
                    "Verification documents are required for registration",
                    HttpStatus.BAD_REQUEST
            );
        }

        // Set initial verification status
        vendor.setVerified(false);

        try {
            return vendorRepository.save(vendor);
        } catch (Exception e) {
            throw new VendorException(
                    "Failed to register vendor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    public Vendor verifyVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorException(
                        "Vendor not found with ID: " + vendorId,
                        HttpStatus.NOT_FOUND
                ));

        // Check if already verified
        if (vendor.isVerified()) {
            throw new VendorException(
                    "Vendor is already verified",
                    HttpStatus.CONFLICT
            );
        }

        // Validate verification documents
        validateVerificationDocuments(vendor);

        // Set verification status
        vendor.setVerified(true);

        try {
            return vendorRepository.save(vendor);
        } catch (Exception e) {
            throw new VendorException(
                    "Failed to verify vendor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void validateVerificationDocuments(Vendor vendor) {
        var docs = vendor.getVerificationDocuments();

        if (docs == null) {
            throw new VendorException(
                    "Verification documents are missing",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (docs.getBusinessRegistrationNumber() == null || docs.getBusinessRegistrationNumber().trim().isEmpty()) {
            throw new VendorException(
                    "Business registration number is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (docs.getTaxIdentificationNumber() == null || docs.getTaxIdentificationNumber().trim().isEmpty()) {
            throw new VendorException(
                    "Tax identification number is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        // Add more document validations as needed
    }
}