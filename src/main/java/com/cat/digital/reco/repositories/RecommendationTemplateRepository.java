package com.cat.digital.reco.repositories;

import java.util.Optional;

import com.cat.digital.reco.domain.entities.RecommendationTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationTemplateRepository extends JpaRepository<RecommendationTemplatesEntity, Integer> {
  Optional<RecommendationTemplatesEntity> findByTemplateName(String name);
}
