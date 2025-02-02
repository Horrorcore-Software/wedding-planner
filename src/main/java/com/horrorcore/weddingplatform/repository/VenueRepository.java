package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.VenueDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VenueRepository extends JpaSpecificationExecutor<VenueDetails>, PagingAndSortingRepository<VenueDetails, Long> {
}
