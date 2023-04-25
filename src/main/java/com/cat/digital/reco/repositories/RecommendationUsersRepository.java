package com.cat.digital.reco.repositories;

import com.cat.digital.reco.domain.entities.RecommendationUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for user related CRUD operations.
 */
@Repository
public interface RecommendationUsersRepository extends
    JpaRepository<RecommendationUsersEntity, String> {
}
