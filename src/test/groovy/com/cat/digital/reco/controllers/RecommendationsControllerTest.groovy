package com.cat.digital.reco.controllers

import com.cat.digital.reco.domain.models.Authorization
import com.cat.digital.reco.domain.models.BaseOwner
import com.cat.digital.reco.domain.models.HoursReading
import com.cat.digital.reco.domain.models.OrderByParam
import com.cat.digital.reco.domain.models.RecommendationSortByParam
import com.cat.digital.reco.domain.models.TemplateCustomField
import com.cat.digital.reco.domain.requests.RecommendationPostRequest
import com.cat.digital.reco.domain.requests.RecommendationPutRequest
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest
import com.cat.digital.reco.domain.responses.Recommendation
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse
import com.cat.digital.reco.service.RecommendationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpHeaders
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.util.ResourceUtils
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import java.time.OffsetDateTime

import static com.cat.digital.reco.common.Constants.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RecommendationsController.class)
class RecommendationsControllerTest extends Specification {

    @SpringBean
    RecommendationService service = Mock()

    @Autowired
    private WebApplicationContext webAppContext

    @Autowired
    private ObjectMapper objectMapper

    @Shared
    def authorization = new Authorization()

    private MockMvc mockMvc

    private static baseUrl = "/recommendations/v1/"

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
    }

    def "create recommendation with 201 response code"() {
        given:
        def property = new TemplateCustomField()
        property.setPropertyName('propertyNae')
        property.setPropertyValue('property value')
        def templateList = new ArrayList<TemplateCustomField>()
        templateList.add(property)
        def recResponse = new RecommendationDetailsResponse()

        def reqBody = objectMapper.writeValueAsString(new RecommendationPostRequest(
                assetId: "assetID",
                templateName: "default",
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: templateList))

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(reqBody)
                .requestAttr("EntitledDealers", authorization)

        service.createRecommendation(_ as RecommendationPostRequest, authorization) >> recResponse

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isCreated())
        result.andExpect(header().exists("Location"))
        result.andExpect(content().json(objectMapper.writeValueAsString(recResponse)))
    }

    def "create recommendation with bad request response code for template field"() {
        given:

        def reqBody = objectMapper.writeValueAsString(new RecommendationPostRequest(
                assetId: "assetID",
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: new ArrayList<TemplateCustomField>()))

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(reqBody)


        service.createRecommendation(_ as RecommendationPostRequest, authorization) >> new RecommendationDetailsResponse()

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "create recommendation with bad request response code for expiration date field are in the past"() {
        given:

        def reqBody = objectMapper.writeValueAsString(new RecommendationPostRequest(
                assetId: "assetID",
                templateName: "default",
                expirationTime: OffsetDateTime.now().minusSeconds(1),
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours")
                ,
                templateCustomProperties: new ArrayList<TemplateCustomField>()))

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(reqBody)


        service.createRecommendation(_ as RecommendationPostRequest, authorization) >> new RecommendationDetailsResponse()

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "create recommendation with bad request response code for expiration date field are more than one year"() {
        given:

            def reqBody = objectMapper.writeValueAsString(new RecommendationPostRequest(
                assetId: "assetID",
                templateName: "default",
                expirationTime: OffsetDateTime.now().plusYears(1).plusSeconds(1),
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours")
                ,
                templateCustomProperties: new ArrayList<TemplateCustomField>()))

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(reqBody)


        service.createRecommendation(_ as RecommendationPostRequest, authorization) >> new RecommendationDetailsResponse()

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "create recommendation with bad request response code for title field have more than 100 characters"() {
        given:

        def reqBody = objectMapper.writeValueAsString(new RecommendationPostRequest(
                assetId: "assetID",
                templateName: "default",
                title: "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901",
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours")
                ,
                templateCustomProperties: new ArrayList<TemplateCustomField>()))

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(reqBody)


        service.createRecommendation(_ as RecommendationPostRequest, authorization) >> new RecommendationDetailsResponse()

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation with a valid request then the response has status 204"() {
        given: "PUT request for recommendation"
        def recommendationPutRequest = new RecommendationPutRequest()
        def hoursReading = new HoursReading()
        hoursReading.unitOfMeasure = 'hours'
        hoursReading.reading = 2000

        recommendationPutRequest.setHoursReading(hoursReading)
        recommendationPutRequest.title = 'Title'
        def owner = new BaseOwner()
        owner.catrecid = 'BBB-98765435'
        recommendationPutRequest.setOwner(owner)
        def customProperties = new ArrayList<TemplateCustomField>()

        recommendationPutRequest.expirationTime = OffsetDateTime.now().plusDays(1);
        recommendationPutRequest.setTemplateCustomProperties(customProperties)
        def recResponse = new RecommendationDetailsResponse()

        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))
                .requestAttr("EntitledDealers", authorization)

        and: "recommendation exists"// "additional invariant clause"
        // combined mock and stub, see: Combining Mocking and Stubbing http://spockframework.org/spock/docs/1.0/interaction_based_testing.html
        1 * service.updateRecommendation(_ as Object, _ as String, authorization) >> recResponse

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().is2xxSuccessful())
        result.andExpect(content().json(objectMapper.writeValueAsString(recResponse)))
    }

    def "Update an existing recommendation when the owner catRecId not populated in request, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.expirationTime = OffsetDateTime.now().plusDays(1);
        recommendationPutRequest.setOwner(new BaseOwner())

        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation with SMU Reading not populated in request, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setUnitOfMeasure('hours')

        recommendationPutRequest.expirationTime = OffsetDateTime.now().plusDays(1);
        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setHoursReading(hoursReading)

        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation when SMU Unit of Measure not populated in request, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setReading(3000 as BigDecimal)
        recommendationPutRequest.expirationTime = OffsetDateTime.now().plusDays(1);
        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setHoursReading(hoursReading)
        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation when Title not populated in request, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setReading(3000 as BigDecimal)
        hoursReading.setUnitOfMeasure('hours')

        recommendationPutRequest.expirationTime = OffsetDateTime.now().plusDays(1);
        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setHoursReading(hoursReading)

        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation when Expiration date not populated in request, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setReading(3000 as BigDecimal)
        hoursReading.setUnitOfMeasure('hours')
        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setTitle('Recommendation Title')
        recommendationPutRequest.setHoursReading(hoursReading)
        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation when Title is more than 100 characters, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setReading(3000 as BigDecimal)
        hoursReading.setUnitOfMeasure('hours')

        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setTitle('A wonderful serenity has taken possession of my entire soul, ' +
                'like these sweet mornings of spring which I enjoy with my whole heart.')
        recommendationPutRequest.setExpirationTime(OffsetDateTime.now().plusDays(2))
        recommendationPutRequest.setHoursReading(hoursReading)

        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Update an existing recommendation when Expiration date is more than a year away form today, it should throw bad request"() {
        given: "PUT recommendation called from service layer"
        def recommendationPutRequest = new RecommendationPutRequest()
        def owner = new BaseOwner()
        owner.setCatrecid('AA-12434')
        def hoursReading = new HoursReading()
        hoursReading.setReading(3000 as BigDecimal)
        hoursReading.setUnitOfMeasure('hours')
        recommendationPutRequest.setOwner(owner)
        recommendationPutRequest.setTitle('Recommendation Title')
        recommendationPutRequest.setExpirationTime(OffsetDateTime.now().plusYears(2))
        recommendationPutRequest.setHoursReading(hoursReading)
        def putRequest = MockMvcRequestBuilders.put(baseUrl + 'recommendations/1')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationPutRequest))

        and: "recommendation exists"// "additional invariant clause"

        when:
        def result = mockMvc.perform(putRequest)

        then:
        result.andExpect(status().isBadRequest())
    }

    def "when get is performed then the response has status 200"() {
        given: "request for recommendation"
        String recommendationUrl = "/recommendations/v1/recommendations/1"
        def recommendationDetails = new RecommendationDetailsResponse()

        and: "recommendation exists"// "additional invariant clause"
        // combined mock and stub, see: Combining Mocking and Stubbing http://spockframework.org/spock/docs/1.0/interaction_based_testing.html
        1 * service.getRecommendationDetails("1", authorization) >> recommendationDetails

        expect: "Status is 200"
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(recommendationUrl)
                        .requestAttr("EntitledDealers", authorization)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    def "when get is performed then the response has status 200 and have the PDF file"() {
        given: "request for recommendation"
        String recommendationUrl = baseUrl + 'recommendations/1'
        InputStreamResource pdfStream = new InputStreamResource(new ByteArrayInputStream(ResourceUtils.getFile("classpath:data/Recommendation_PDF.pdf").readBytes()))

        and: "recommendation exists"// "additional invariant clause"
        // combined mock and stub, see: Combining Mocking and Stubbing http://spockframework.org/spock/docs/1.0/interaction_based_testing.html
        1 * service.getRecommendationPDF("1", authorization) >> pdfStream

        expect: "Status is 200"
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(recommendationUrl)
                        .requestAttr("EntitledDealers", authorization)
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
    }

    def "when searching for recommendation get a 200 response"() {
        given:
        def searchRequest = new RecommendationSearchRequest()
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>()
        queryParams.add(SORT_BY, "serialNumber");
        queryParams.add(ORDER_BY, "ASC");
        queryParams.add(CURSOR, "23da034e-a4e3-11ea-bb37-0242ac130002");
        queryParams.add(LIMIT, "25");
        queryParams.add(SKIP, "10");
        queryParams.add(SEARCH_VALUE, "make");

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/search')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(queryParams)
                .requestAttr("EntitledDealers", authorization)
                .content(objectMapper.writeValueAsString(searchRequest))


        def sortByParam = Arrays.asList(RecommendationSortByParam.SERIAL_NUMBER)
        def orderByParam = Arrays.asList(OrderByParam.ASC)
        service.getRecommendations(authorization, sortByParam, orderByParam, searchRequest) >> Collections.singletonList(new Recommendation().builder().serialNumber("1").build())

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().is2xxSuccessful())
    }

    def "when searching for recommendation get a 404 response"() {
        given:
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>()
        queryParams.add(SORT_BY, "serialNumber");
        queryParams.add(ORDER_BY, "ASC");
        queryParams.add(CURSOR, "23da034e-a4e3-11ea-bb37-0242ac130002");
        queryParams.add(LIMIT, "25");
        queryParams.add(SKIP, "10");
        queryParams.add(SEARCH_VALUE, "make");

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/search')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(queryParams)
                .requestAttr("EntitledDealers", authorization)
                .content(objectMapper.writeValueAsString(RecommendationSearchRequest.builder().build()))

        service.getRecommendations(authorization) >> Collections.emptyList()

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().is4xxClientError())
    }

    def "when exporting recommendation get a 200 response"() {
        given:
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>()
        queryParams.add(SORT_BY, "serialNumber");
        queryParams.add(ORDER_BY, "ASC");
        queryParams.add(CURSOR, "23da034e-a4e3-11ea-bb37-0242ac130002");
        queryParams.add(LIMIT, "25");
        queryParams.add(SKIP, "10");
        queryParams.add(SEARCH_VALUE, "make");

        def req = MockMvcRequestBuilders.post(baseUrl + 'recommendations/search')
                .header("x-cat-entitlements", "")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .contentType(MediaType.APPLICATION_JSON)
                .params(queryParams)
                .requestAttr("EntitledDealers", authorization)
                .content(objectMapper.writeValueAsString(RecommendationSearchRequest.builder().build()))

        def sortByParam = Arrays.asList(RecommendationSortByParam.SERIAL_NUMBER)
        def orderByParam = Arrays.asList(OrderByParam.ASC)
        service.getRecommendations(authorization, sortByParam, orderByParam) >> Collections.singletonList(new Recommendation().builder().serialNumber("1").build())

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().is2xxSuccessful())
    }
}
