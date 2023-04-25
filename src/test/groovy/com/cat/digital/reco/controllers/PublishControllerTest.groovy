package com.cat.digital.reco.controllers

import com.cat.digital.reco.dao.NotificationDao
import com.cat.digital.reco.domain.models.Recipients
import com.cat.digital.reco.domain.requests.RecommendationPublishRequest
import com.cat.digital.reco.service.PublishService
import com.cat.digital.reco.service.RecommendationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PublishController.class)
class PublishControllerTest extends Specification {

    @SpringBean
    PublishService service = Mock()

    @SpringBean
    NotificationDao dao = Mock()

    @SpringBean
    RecommendationService recommendationService = Mock()


    @Autowired
    private WebApplicationContext webAppContext

    @Autowired
    private ObjectMapper objectMapper

    private PublishController publishController

    private MockMvc mockMvc

    private static baseUrl = "/recommendations/v1/"

    def setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build()
        publishController = new PublishController(service)
    }

    ArrayList<String> to = new ArrayList<String>() {
        {
            add("user.name@cat.com");
        }
    }

    ArrayList<String> ccValid = new ArrayList<String>() {
        {
            add("user.name2@cat.com");
        }
    }

    ArrayList<String> invalidEmail = new ArrayList<String>() {
        {
            add(" @cat.com");
            add("user.name.com")
        }
    }

    ArrayList<String> listEmpty = new ArrayList<String>()


    // TESTS for POST /recommendations/{recommendationNumber}/publish
    def "Test publish recommendation successfully"() {
        given:
        ArrayList<String> to = new ArrayList<String>() {
            {
                add("user.name@cat.com");
            }
        }

        def recommendationNumber = UUID.fromString("57661b35-6f47-4732-9dd1-49c6fee27143")

        def requestBody = objectMapper.writeValueAsString(new RecommendationPublishRequest(
                subject: "Recommendation title",
                sendCopyToSender: true,
                recipients: new Recipients(
                        to: to,
                        cc: ccValid
                )

        ))
        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/{recommendationNumber}/publish', recommendationNumber)
                .header("x-cat-entitlements", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        service.publishRecommendation(_ as Object, recommendationNumber) >> true

        when:
        def result = mockMvc.perform(req)
        result.andDo(MockMvcResultHandlers.print())

        then:
        result.andExpect(status().isNoContent())
    }

    def "Test publish recommendation Bad Request missing subject"() {
        given:

        def recommendationNumber = UUID.fromString("57661b35-6f47-4732-9dd1-49c6fee27143")

        def requestBody = objectMapper.writeValueAsString(new RecommendationPublishRequest(
                sendCopyToSender: true,
                recipients: new Recipients(
                        to: to
                )

        ))
        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/{recommendationNumber}/publish', recommendationNumber)
                .header("x-cat-entitlements", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        service.publishRecommendation(_ as Object, recommendationNumber) >> true

        when:
        def result = mockMvc.perform(req)
        result.andDo(MockMvcResultHandlers.print())

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Test publish recommendation Bad Request missing to recipient"() {
        given:
        def recommendationNumber = UUID.fromString("57661b35-6f47-4732-9dd1-49c6fee27143")

        def requestBody = objectMapper.writeValueAsString(new RecommendationPublishRequest(
                sendCopyToSender: true,
                recipients: new Recipients(
                )

        ))
        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/{recommendationNumber}/publish', recommendationNumber)
                .header("x-cat-entitlements", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        service.publishRecommendation(_ as Object, recommendationNumber) >> true

        when:
        def result = mockMvc.perform(req)
        result.andDo(MockMvcResultHandlers.print())

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Test publish recommendation Bad Request invalid cc address recipient"() {
        given:
        def recommendationNumber = UUID.fromString("57661b35-6f47-4732-9dd1-49c6fee27143")

        def requestBody = objectMapper.writeValueAsString(new RecommendationPublishRequest(
                sendCopyToSender: true,
                recipients: new Recipients(
                        to: to,
                        cc: invalidEmail
                )

        ))
        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/{recommendationNumber}/publish', recommendationNumber)
                .header("x-cat-entitlements", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        service.publishRecommendation(_ as Object, recommendationNumber) >> true

        when:
        def result = mockMvc.perform(req)
        result.andDo(MockMvcResultHandlers.print())

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Test publish recommendation Bad Request empty cc address recipient"() {
        given:
        def recommendationNumber = UUID.fromString("57661b35-6f47-4732-9dd1-49c6fee27143")

        def requestBody = objectMapper.writeValueAsString(new RecommendationPublishRequest(
                sendCopyToSender: true,
                recipients: new Recipients(
                        to: to,
                        cc: new ArrayList<String>()
                )

        ))
        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/{recommendationNumber}/publish', recommendationNumber)
                .header("x-cat-entitlements", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        service.publishRecommendation(_ as Object, recommendationNumber) >> true

        when:
        def result = mockMvc.perform(req)
        result.andDo(MockMvcResultHandlers.print())

        then:
        result.andExpect(status().isBadRequest())
    }


}
