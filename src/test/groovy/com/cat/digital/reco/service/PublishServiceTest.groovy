package com.cat.digital.reco.service

import com.cat.digital.reco.common.CustomResponseCodes
import com.cat.digital.reco.dao.NotificationDao
import com.cat.digital.reco.domain.requests.RecommendationPublishRequest
import com.cat.digital.reco.exceptions.InvalidInputRequestException
import com.cat.digital.reco.exceptions.RecoServerException
import com.cat.digital.reco.service.impl.PublishServiceImpl

import spock.lang.Specification

class PublishServiceTest extends Specification {

    NotificationDao notificationDao
    RecommendationService recommendationService
    PublishService publishService
    RecommendationPublishRequest publishRequest
    def setup() {
        notificationDao = Mock();
        recommendationService = Mock()
        publishRequest = new RecommendationPublishRequest();
        publishRequest.setMessage("Email content")
    }

    //TESTS for publish recommendation service layer
    def "Test publish recommendation service successfully"() {
        given:

        publishService = new PublishServiceImpl(notificationDao, recommendationService);
        notificationDao.sendNotification(_ as Object) >> true
        String recommendationNumber = "REC-123-456-789"
        when:
        def result = publishService.publishRecommendation(new RecommendationPublishRequest(), recommendationNumber)

        then:
        assert result != null
    }

    //TESTS for publish recommendation service layer
    def "Test publish recommendation when recommendation doesn't exist"() {
        given:

        publishService = new PublishServiceImpl(notificationDao, recommendationService);
        recommendationService.validateRecommendationExists(*_) >> {
            throw new InvalidInputRequestException(CustomResponseCodes.NOT_FOUND, "Recommendation doesn't exist")
        }
        String recommendationNumber = "REC-123-456-789"
        when:
        def result = publishService.publishRecommendation(new RecommendationPublishRequest(), recommendationNumber)
        then:
        thrown InvalidInputRequestException
    }

    //TESTS for publish recommendation service layer
    def "Test publish recommendation throw RecoServerException"() {
        given:
        publishService = new PublishServiceImpl(notificationDao, recommendationService);
        notificationDao.sendNotification(_ as Object) >> {
            throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "Internal Server error")
        }
        String recommendationNumber = "REC-123-456-789"
        when:
        def result = publishService.publishRecommendation(new RecommendationPublishRequest(), recommendationNumber)
        then:
        thrown RecoServerException
    }

}
