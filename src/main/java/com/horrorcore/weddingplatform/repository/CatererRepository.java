package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.CatererDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CatererRepository extends JpaSpecificationExecutor<CatererDetails>, PagingAndSortingRepository<CatererDetails, Long> {
    List<CatererDetails> findByCuisineTypesContaining(String cuisineType);
    List<CatererDetails> findByPerPlateStartingCostLessThanEqual(Double maxCost);
    List<CatererDetails> findByMaximumGuestCountGreaterThanEqual(Integer guestCount);
}