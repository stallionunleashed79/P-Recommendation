package com.cat.digital.reco.service

import com.cat.digital.reco.domain.models.OrderByParam
import com.cat.digital.reco.domain.models.RecommendationSortByParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Sort
import spock.lang.Specification

@WebMvcTest(RecommendationSortService.class)
class RecommendationSortServiceTest extends Specification {
    @Autowired
    RecommendationSortService recommendationSortService

    def "getRecommendationSort returns unsorted for empty params"() {
        expect:
        recommendationSortService.getRecommendationSort([], []) == Sort.unsorted()
    }

    def "getRecommendationSort returns the correct sort for single param"() {
        expect:
        recommendationSortService.getRecommendationSort([column], [direction]) == sort

        where:
        column                                              | direction          || sort
        RecommendationSortByParam.SERIAL_NUMBER                 | OrderByParam.ASC  || Sort.by("recommendationCommonData.serialNumber").ascending()
        RecommendationSortByParam.MAKE                          | OrderByParam.ASC  || Sort.by("recommendationCommonData.make").ascending()
        RecommendationSortByParam.MODEL                         | OrderByParam.ASC  || Sort.by("recommendationCommonData.model").ascending()
        RecommendationSortByParam.CREATED_TIME                  | OrderByParam.ASC  || Sort.by("recommendationCommonData.createdDate").ascending()
        RecommendationSortByParam.TITLE                         | OrderByParam.ASC  || Sort.by("recommendationCommonData.title").ascending()
        RecommendationSortByParam.RECOMMENDATION_PRIORITY       | OrderByParam.ASC  || Sort.by("recommendationSort.recommendationPriority").ascending()
        RecommendationSortByParam.RECOMMENDATION_STATUS         | OrderByParam.ASC  || Sort.by("recommendationSort.recommendationStatus").ascending()
        RecommendationSortByParam.EXPIRATION_TIME               | OrderByParam.ASC  || Sort.by("recommendationCommonData.expirationDate").ascending()
        RecommendationSortByParam.UCID_NAME                     | OrderByParam.ASC  || Sort.by("recommendationCommonData.customerUcid").ascending()
        RecommendationSortByParam.SITE                          | OrderByParam.ASC  || Sort.by("recommendationCommonData.site").ascending()
        RecommendationSortByParam.ASSET_ID                      | OrderByParam.ASC  || Sort.by("recommendationCommonData.assetName").ascending()
        RecommendationSortByParam.OWNER                         | OrderByParam.ASC  || Sort.by("recommendationCommonData.ownedBy.catrecid").ascending()
        RecommendationSortByParam.RECOMMENDATION_NUMBER         | OrderByParam.ASC  || Sort.by("recommendationNumber").ascending()
        RecommendationSortByParam.TEMPLATE_NAME                 | OrderByParam.ASC  || Sort.by("recommendationCommonData.template.templateName").ascending()
        RecommendationSortByParam.WORK_ORDER_ID                 | OrderByParam.ASC  || Sort.by("recommendationSort.workOrderNumber").ascending()
        RecommendationSortByParam.NUMBER_OF_DAYS_SINCE_MODIFIED | OrderByParam.ASC  || Sort.by("recommendationCommonData.updatedDate").descending()
        // to sort by number of days, using opposite direction

        RecommendationSortByParam.SERIAL_NUMBER                 | OrderByParam.DESC || Sort.by("recommendationCommonData.serialNumber").descending()
        RecommendationSortByParam.MAKE                          | OrderByParam.DESC || Sort.by("recommendationCommonData.make").descending()
        RecommendationSortByParam.MODEL                         | OrderByParam.DESC || Sort.by("recommendationCommonData.model").descending()
        RecommendationSortByParam.CREATED_TIME                  | OrderByParam.DESC || Sort.by("recommendationCommonData.createdDate").descending()
        RecommendationSortByParam.TITLE                         | OrderByParam.DESC || Sort.by("recommendationCommonData.title").descending()
        RecommendationSortByParam.RECOMMENDATION_PRIORITY       | OrderByParam.DESC || Sort.by("recommendationSort.recommendationPriority").descending()
        RecommendationSortByParam.RECOMMENDATION_STATUS         | OrderByParam.DESC || Sort.by("recommendationSort.recommendationStatus").descending()
        RecommendationSortByParam.EXPIRATION_TIME               | OrderByParam.DESC || Sort.by("recommendationCommonData.expirationDate").descending()
        RecommendationSortByParam.UCID_NAME                     | OrderByParam.DESC || Sort.by("recommendationCommonData.customerUcid").descending()
        RecommendationSortByParam.SITE                          | OrderByParam.DESC || Sort.by("recommendationCommonData.site").descending()
        RecommendationSortByParam.ASSET_ID                      | OrderByParam.DESC || Sort.by("recommendationCommonData.assetName").descending()
        RecommendationSortByParam.OWNER                         | OrderByParam.DESC || Sort.by("recommendationCommonData.ownedBy.catrecid").descending()
        RecommendationSortByParam.RECOMMENDATION_NUMBER         | OrderByParam.DESC || Sort.by("recommendationNumber").descending()
        RecommendationSortByParam.TEMPLATE_NAME                 | OrderByParam.DESC || Sort.by("recommendationCommonData.template.templateName").descending()
        RecommendationSortByParam.WORK_ORDER_ID                 | OrderByParam.DESC || Sort.by("recommendationSort.workOrderNumber").descending()
        RecommendationSortByParam.NUMBER_OF_DAYS_SINCE_MODIFIED | OrderByParam.DESC || Sort.by("recommendationCommonData.updatedDate").ascending()
    }
}
