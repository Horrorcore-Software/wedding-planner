package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.PhotographerDetails;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PhotographerRepository extends JpaSpecificationExecutor<PhotographerDetails>, PagingAndSortingRepository<PhotographerDetails, Long> {
    List<PhotographerDetails> findByDronePhotographyAvailable(boolean available);
    List<PhotographerDetails> findByVideoServicesAvailable(boolean available);

    PhotographerDetails save(PhotographerDetails details);
}