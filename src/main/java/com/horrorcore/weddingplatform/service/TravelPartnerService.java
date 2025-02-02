package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.TravelPartnerDetails;
import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.repository.TravelPartnerRepository;
import com.horrorcore.weddingplatform.repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelPartnerService {
    private final TravelPartnerRepository travelPartnerRepository;
    private final VendorRepository vendorRepository;

    public TravelPartnerService(TravelPartnerRepository travelPartnerRepository,
                                VendorRepository vendorRepository) {
        this.travelPartnerRepository = travelPartnerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public TravelPartnerDetails registerTravelPartner(Long vendorId, TravelPartnerDetails details) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorException("Vendor not found", HttpStatus.NOT_FOUND));

        validateTravelPartnerDetails(details);
        details.setVendor(vendor);
        return travelPartnerRepository.save(details);
    }

    private void validateTravelPartnerDetails(TravelPartnerDetails details) {
        if (details.getFleetTypes() == null || details.getFleetTypes().isEmpty()) {
            throw new VendorException("At least one fleet type must be specified", HttpStatus.BAD_REQUEST);
        }

        if (details.getTotalFleetSize() < 1) {
            throw new VendorException("Total fleet size must be at least 1", HttpStatus.BAD_REQUEST);
        }

        if (details.getAdvanceBookingDays() < 1) {
            throw new VendorException("Advance booking days must be at least 1", HttpStatus.BAD_REQUEST);
        }

        if (details.getInsuranceDetails() == null || details.getInsuranceDetails().trim().isEmpty()) {
            throw new VendorException("Insurance details are required", HttpStatus.BAD_REQUEST);
        }
    }

    public List<TravelPartnerDetails> findByServiceArea(String area) {
        return List.of();
    }

    public List<TravelPartnerDetails> findByFleetType(String fleetType) {
        return List.of();
    }
}
