package com.cat.digital.reco.repositories;

import java.util.Optional;

import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity;
import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository class for the recommendation view CRUD operations.
 */
@Repository
public interface RecommendationDynamicSortHelperRepository extends JpaRepository<RecommendationDynamicDataSortHelper, Integer>,
    JpaSpecificationExecutor<RecommendationDynamicDataSortHelper> {
}
