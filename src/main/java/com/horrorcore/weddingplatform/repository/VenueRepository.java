package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.Vendor;
import com.horrorcore.weddingplatform.model.VenueDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface VenueRepository extends JpaSpecificationExecutor<VenueDetails>, PagingAndSortingRepository<VenueDetails, Long> {
    Optional<VenueDetails> findByVendor(Vendor vendor);
}
