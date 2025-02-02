package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByEmail(String email);
    boolean existsByBusinessName(String businessName);
}
