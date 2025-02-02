package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.DecoratorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DecoratorRepository extends JpaSpecificationExecutor<DecoratorDetails>, PagingAndSortingRepository<DecoratorDetails, Long> {
    List<DecoratorDetails> findByDecorationStylesContaining(String style);
    List<DecoratorDetails> findByProvidesCustomDesigns(boolean providesCustomDesigns);
    List<DecoratorDetails> findBySpecializationsContaining(String specialization);
}
