package com.cat.digital.reco.repositories;

import java.util.List;

import com.cat.digital.reco.domain.entities.RecommendationAuditsEntity;
import com.cat.digital.reco.domain.entities.pk.RecommendationAuditsEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationAuditRepository extends JpaRepository<RecommendationAuditsEntity, RecommendationAuditsEntityPK> {

  @Query(value = "SELECT a.* FROM recommendation_audits a "
      + "INNER JOIN ("
      + "    SELECT n.recommendation_id, "
      + "    n.recommendation_field_id, "
      + "    MAX(n.id) as id "
      + "    FROM recommendation_audits n "
      + "    WHERE n.recommendation_id = :recommendationId "
      + "    GROUP BY recommendation_id, recommendation_field_id "
      + ") tm on "
      + "a.recommendation_id = tm.recommendation_id "
      + "AND a.recommendation_field_id = tm.recommendation_field_id "
      + "AND a.id = tm.id", nativeQuery = true)
  List<RecommendationAuditsEntity> listLastsUpdates(@Param("recommendationId") Integer recommendationId);
}
