package com.cat.digital.reco.dao

import com.cat.digital.reco.dao.impl.NotificationDaoImpl
import com.cat.digital.reco.domain.requests.RecommendationPublishRequest
import com.cat.digital.reco.utils.rest.OkResterImpl

import okhttp3.Request
import okhttp3.Response
import okhttp3.Protocol
import okhttp3.ResponseBody
import org.springframework.http.MediaType
import spock.lang.Specification

class NotificationDaoTest extends Specification {

    NotificationDao notificationDao;
    OkResterImpl okRester
    String url = 'http://localhost:8080'
    String path = "/myResourcePath"
    def setup() {
        okRester = Mock()
        notificationDao =  new NotificationDaoImpl(okRester, url, path)
    }

    def "test send notification request"() {
        given:
        def recommendationPublishRequest = new RecommendationPublishRequest()
        okRester.makeRequest(_ as Request) >> generateResponse(200)

        when:
        def result = notificationDao.sendNotification(recommendationPublishRequest);

        then:
        result
    }

    def "test send notification fail"() {
        given:
        def recommendationPublishRequest = new RecommendationPublishRequest()
        okRester.makeRequest(_ as Request) >> generateResponse(400)

        when:
        def result = notificationDao.sendNotification(recommendationPublishRequest);

        then:
        !result
    }

    def generateResponse(int status) {
        def response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(status).message("test").header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .body(ResponseBody.create(
                okhttp3.MediaType.parse("application/json"), "")).build()

        return response
    }
}
