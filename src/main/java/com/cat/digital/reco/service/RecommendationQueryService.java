package com.cat.digital.reco.service;

import java.util.List;

import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import org.springframework.data.domain.Sort;

/**
 * Queries the recommendation database and filters recommendations by search criteria.
 */
public interface RecommendationQueryService {
  List<RecommendationDynamicDataSortHelper> filterRecommendations(
      final Authorization authorization,
      final RecommendationSearchRequest recommendationSearchRequest,
      final Sort recommendationSort);
}
