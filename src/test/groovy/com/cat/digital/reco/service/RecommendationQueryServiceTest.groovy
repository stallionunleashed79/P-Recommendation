package com.cat.digital.reco.service

import com.cat.digital.reco.domain.entities.RecommendationCollectionOptionsEntity
import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity
import com.cat.digital.reco.domain.entities.RecommendationCustomDataEntity
import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper
import com.cat.digital.reco.domain.entities.RecommendationFieldCollectionsEntity
import com.cat.digital.reco.domain.entities.RecommendationFieldsEntity
import com.cat.digital.reco.domain.entities.RecommendationTemplatesEntity
import com.cat.digital.reco.domain.entities.RecommendationUsersEntity
import com.cat.digital.reco.domain.models.Authorization
import com.cat.digital.reco.domain.models.RecommendationField
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest
import com.cat.digital.reco.exceptions.CustomMethodArgumentNotValidException
import com.cat.digital.reco.filter.type.ContainsFilter
import com.cat.digital.reco.filter.type.StringEqualsFilter
import com.cat.digital.reco.repositories.RecommendationCollectionOptionsRepository
import com.cat.digital.reco.repositories.RecommendationDynamicSortHelperRepository
import io.cucumber.messages.internal.com.google.common.collect.Lists
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Sort
import spock.lang.Specification

import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

@WebMvcTest(RecommendationQueryService.class)
class RecommendationQueryServiceTest extends Specification {

    @SpringBean
    RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository = Mock()

    @SpringBean
    RecommendationCollectionOptionsRepository recommendationCollectionOptionsRepository = Mock()

    @Autowired
    RecommendationQueryService recommendationQueryService

    static def assetId = "CAT|RXZ00353|2969412354"

    def "Query recommendations by filter criteria for containsFilter as a CAT user"() {
        given:
        def authorization = new Authorization()
        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def sortBody = Sort.by("sortBy").descending()
        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("0").optionName("Draft").build()
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .recommendationNumber("REC-112-113")
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName(
                "Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()

        def containsFilter = new ContainsFilter()
        containsFilter.propertyName = RecommendationField.TITLE.value
        containsFilter.value = 'REC'
        def filterList = List.of(containsFilter);
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationQueryService.filterRecommendations(authorization, searchRequest, sortBody)

        then:
        1 * recommendationCollectionOptionsRepository.findAll() >> List.of(recommendationStatusOptions)
        1 * recommendationDynamicSortHelperRepository.findAll(_, sortBody) >> List.of(recommendationDynamicSortHelper)
        assert result != null
    }

    def "Query recommendations by filter criteria for stringEqualsFilter as a CAT user"() {
        given:
        def authorization = new Authorization()
        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def sortBody = Sort.by("sortBy").descending()
        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("0").optionName("Draft").build()
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .recommendationNumber("REC-112-113")
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName(
                "Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()

        def containsFilter = new StringEqualsFilter()
        containsFilter.propertyName = RecommendationField.RECOMMENDATION_STATUS.value
        containsFilter.values = ['Draft']
        def filterList = List.of(containsFilter);
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationQueryService.filterRecommendations(authorization, searchRequest, sortBody)

        then:
        1 * recommendationCollectionOptionsRepository.findAll() >> List.of(recommendationStatusOptions)
        1 * recommendationDynamicSortHelperRepository.findAll(_, sortBody) >> List.of(recommendationDynamicSortHelper)
        assert result != null
    }

    def "Query recommendations by filter criteria for stringEqualsFilter as a dealer user"() {
        given:
        def authorization = new Authorization()
        authorization.setDealer(true)
        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def sortBody = Sort.by("sortBy").descending()
        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("0").optionName("Draft").build()
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .recommendationNumber("REC-112-113")
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName(
                "Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()

        def stringEqualsFilter = new StringEqualsFilter()
        stringEqualsFilter.propertyName = RecommendationField.RECOMMENDATION_STATUS.value
        stringEqualsFilter.values = ['Draft']
        def filterList = Lists.newArrayList(stringEqualsFilter)
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationQueryService.filterRecommendations(authorization, searchRequest, sortBody)

        then:
        1 * recommendationCollectionOptionsRepository.findAll() >> List.of(recommendationStatusOptions)
        1 * recommendationDynamicSortHelperRepository.findAll(_, sortBody) >> List.of(recommendationDynamicSortHelper)
        assert result != null
    }

    def "Query recommendations with invalid stringEqualsFilter fields as a dealer user"() {
        given:
        def authorization = new Authorization()
        authorization.setDealer(true)
        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def sortBody = Sort.by("sortBy").descending()
        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("0").optionName("Draft").build()
        def stringEqualsFilter = new StringEqualsFilter()
        stringEqualsFilter.propertyName = RecommendationField.TITLE.value
        stringEqualsFilter.values = ['Draft']
        def filterList = Lists.newArrayList(stringEqualsFilter)
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        recommendationQueryService.filterRecommendations(authorization, searchRequest, sortBody)

        then:
        1 * recommendationCollectionOptionsRepository.findAll() >> List.of(recommendationStatusOptions)
        thrown CustomMethodArgumentNotValidException
    }

    def "Query recommendations with invalid containsFilter fields as a dealer user"() {
        given:
        def authorization = new Authorization()
        authorization.setDealer(true)
        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def sortBody = Sort.by("sortBy").descending()
        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("0").optionName("Draft").build()
        def stringEqualsFilter = new ContainsFilter()
        stringEqualsFilter.propertyName = RecommendationField.RECOMMENDATION_STATUS.value
        stringEqualsFilter.value = 'REC'
        def filterList = Lists.newArrayList(stringEqualsFilter)
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        recommendationQueryService.filterRecommendations(authorization, searchRequest, sortBody)

        then:
        1 * recommendationCollectionOptionsRepository.findAll() >> List.of(recommendationStatusOptions)
        thrown CustomMethodArgumentNotValidException
    }
}
