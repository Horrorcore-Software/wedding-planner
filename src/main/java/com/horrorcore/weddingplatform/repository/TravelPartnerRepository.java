package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.TravelPartnerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TravelPartnerRepository extends JpaSpecificationExecutor<TravelPartnerDetails>, PagingAndSortingRepository<TravelPartnerDetails, Long> {
    List<TravelPartnerDetails> findByProvidesChauffeur(boolean providesChauffeur);
    List<TravelPartnerDetails> findByServiceAreasContaining(String area);
    List<TravelPartnerDetails> findByFleetTypesContaining(String fleetType);
}