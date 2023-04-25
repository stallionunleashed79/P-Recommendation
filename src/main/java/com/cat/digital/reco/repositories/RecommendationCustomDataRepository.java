package com.cat.digital.reco.repositories;

import com.cat.digital.reco.domain.entities.RecommendationCustomDataEntity;
import com.cat.digital.reco.domain.entities.pk.RecommendationCustomDataEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationCustomDataRepository extends
    JpaRepository<RecommendationCustomDataEntity, RecommendationCustomDataEntityPK> {
}
