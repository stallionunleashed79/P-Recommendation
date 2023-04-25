package com.cat.digital.reco.service.impl;

import java.io.IOException;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.dao.NotificationDao;
import com.cat.digital.reco.domain.requests.RecommendationPublishRequest;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.service.PublishService;

import com.cat.digital.reco.service.RecommendationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PublishServiceImpl implements PublishService {


  private final NotificationDao notificationDao;
  private final RecommendationService recommendationService;

  @Autowired
  public PublishServiceImpl(NotificationDao notificationDao, RecommendationService recommendationService) {
    this.notificationDao = notificationDao;
    this.recommendationService = recommendationService;
  }

  @Override
  public boolean publishRecommendation(RecommendationPublishRequest request,
      String recommendationNumber) {
    boolean result;
    recommendationService.validateRecommendationExists(recommendationNumber,  CustomResponseCodes.NOT_FOUND);
    try {
      result = notificationDao.sendNotification(request);
    } catch (IOException exception) {
      log.error("Error calling to notification service......");
      throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
    return result;
  }

}
