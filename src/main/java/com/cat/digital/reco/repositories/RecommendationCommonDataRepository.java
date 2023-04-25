package com.cat.digital.reco.repositories;

import java.util.Optional;

import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for the recommendation CRUD operations.
 */
@Repository
public interface RecommendationCommonDataRepository extends JpaRepository<RecommendationCommonDataEntity, Integer> {
  Optional<RecommendationCommonDataEntity> findByRecommendationNumber(final String number);

  boolean existsByRecommendationNumber(final String number);
}
