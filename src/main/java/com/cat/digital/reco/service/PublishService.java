package com.cat.digital.reco.service;

import com.cat.digital.reco.domain.requests.RecommendationPublishRequest;


public interface PublishService {

  boolean publishRecommendation(RecommendationPublishRequest request, String recommendationNumber);
}
