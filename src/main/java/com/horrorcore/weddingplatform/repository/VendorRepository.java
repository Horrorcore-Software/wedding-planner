package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface VendorRepository extends JpaSpecificationExecutor<Vendor>, PagingAndSortingRepository<Vendor, Long> {
    boolean existsByEmail(String email);
    boolean existsByBusinessName(String businessName);

    Optional<Vendor> findById(Long vendorId);
}
