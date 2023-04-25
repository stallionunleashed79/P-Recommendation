package com.cat.digital.reco.repositories;

import com.cat.digital.reco.domain.entities.RecommendationCollectionOptionsEntity;
import com.cat.digital.reco.domain.entities.RecommendationUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for recommendation collection options.
 */
@Repository
public interface RecommendationCollectionOptionsRepository extends
    JpaRepository<RecommendationCollectionOptionsEntity, Integer> {
}
