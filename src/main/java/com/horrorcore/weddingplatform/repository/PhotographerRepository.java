package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.PhotographerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PhotographerRepository extends JpaRepository<PhotographerDetails, Long> {
    List<PhotographerDetails> findByDronePhotographyAvailable(boolean available);
    List<PhotographerDetails> findByVideoServicesAvailable(boolean available);
    List<PhotographerDetails> findByBasePackagePriceLessThanEqual(Double maxPrice);
}