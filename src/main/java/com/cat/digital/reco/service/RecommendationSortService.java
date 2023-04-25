package com.cat.digital.reco.service;

import java.util.List;

import com.cat.digital.reco.domain.models.OrderByParam;
import com.cat.digital.reco.domain.models.RecommendationSortByParam;
import org.springframework.data.domain.Sort;


public interface RecommendationSortService {
  Sort getRecommendationSort(final List<RecommendationSortByParam> column, final List<OrderByParam> direction);
}
