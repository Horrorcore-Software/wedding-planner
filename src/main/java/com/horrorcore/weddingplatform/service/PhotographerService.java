package com.horrorcore.weddingplatform.service;


import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.model.PhotographerDetails;
import com.horrorcore.weddingplatform.repository.VendorRepository;
import com.horrorcore.weddingplatform.repository.PhotographerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class PhotographerService {
    private final PhotographerRepository photographerRepository;
    private final VendorRepository vendorRepository;

    public PhotographerService(PhotographerRepository photographerRepository, VendorRepository vendorRepository) {
        this.photographerRepository = photographerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public PhotographerDetails registerPhotographer(Long vendorId, PhotographerDetails details) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorException("Vendor not found", HttpStatus.NOT_FOUND));

        if (details.getPhotographyPackages() == null || details.getPhotographyPackages().isEmpty()) {
            throw new VendorException("At least one photography package is required", HttpStatus.BAD_REQUEST);
        }

        validatePhotographerDetails(details);
        details.setVendor(vendor);
        return photographerRepository.save(details);
    }

    private void validatePhotographerDetails(PhotographerDetails details) {
        if (details.getMinimumBookingHours() != null && details.getMinimumBookingHours() < 1) {
            throw new VendorException("Minimum booking hours must be at least 1", HttpStatus.BAD_REQUEST);
        }

        if (details.getBasePrice() != null && details.getBasePrice() <= 0) {
            throw new VendorException("Base price must be greater than 0", HttpStatus.BAD_REQUEST);
        }
    }
}