package com.cat.digital.reco.dao;

import java.io.IOException;

import com.cat.digital.reco.domain.requests.RecommendationPublishRequest;

public interface NotificationDao {

  Boolean sendNotification(RecommendationPublishRequest request) throws IOException;


}
