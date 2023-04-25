package com.cat.digital.reco.service

import com.cat.digital.reco.domain.entities.RecommendationAuditsEntity
import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity
import com.cat.digital.reco.domain.entities.RecommendationFieldsEntity
import com.cat.digital.reco.domain.models.BaseOwner
import com.cat.digital.reco.domain.models.HoursReading
import com.cat.digital.reco.domain.requests.RecommendationPutRequest
import com.cat.digital.reco.repositories.RecommendationAuditRepository
import com.cat.digital.reco.service.impl.AuditServiceImpl
import spock.lang.Specification

import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

class AuditServiceTest extends Specification {

    def auditRepository = Mock(RecommendationAuditRepository.class)
    def defaultTimeZone = "UTC"
    def auditList = new ArrayList()

    AuditServiceImpl auditService
    def setup() {
        auditService = new AuditServiceImpl(defaultTimeZone, auditRepository)
        auditRepository.listLastsUpdates(_ as Integer) >> auditList
    }

    def "payload have a new property to register"() {
        given:
        def commonDataEntity = buildCommonDataEntity()
        def templateFields = buildTemplateFields()
        def putPayload = buildPutRequestMinimalData()

        commonDataEntity.title = null

        auditList.clear()

        when:
        auditService.setAuditFields(putPayload, commonDataEntity, templateFields)

        then: "expect the new property is not present in the audit list"
        assert commonDataEntity.getRecommendationAudits().isEmpty()
    }

    def "payload remove a property already register"() {
        given:
        def commonDataEntity = buildCommonDataEntity()
        def templateFields = buildTemplateFields()
        def putPayload = buildPutRequestMinimalData()

        putPayload.title = null

        auditList.clear()

        when:
        auditService.setAuditFields(putPayload, commonDataEntity, templateFields)

        then: "expect the new property is not present in the audit list"
        assert commonDataEntity.getRecommendationAudits().size() == 1
    }

    def "payload update value of a property already register"() {
        given:
        def commonDataEntity = buildCommonDataEntity()
        def templateFields = buildTemplateFields()
        def putPayload = buildPutRequestMinimalData()

        putPayload.title = "new title"

        auditList.add(new RecommendationAuditsEntity(
                recommendationField: new RecommendationFieldsEntity(fieldName: "title"),
                recommendationFieldId: 1,
                recommendationId: 1,
                id: 1,
                newValue: "old title"
        ))

        when:
        auditService.setAuditFields(putPayload, commonDataEntity, templateFields)

        then: "expect the new property is not present in the audit list"
        assert commonDataEntity.getRecommendationAudits().size() == 1
        assert commonDataEntity.getRecommendationAudits().get(0).getId() == 2
        assert commonDataEntity.getRecommendationAudits().get(0).getNewValue() == "new title"
        assert commonDataEntity.getRecommendationAudits().get(0).getOldValue() == "old title"
    }

    /*******************************************************************************************************************
     ************************************************* HELPER METHODS **************************************************
     ******************************************************************************************************************/
    def buildCommonDataEntity() {
        def commonData = new RecommendationCommonDataEntity()
        commonData.title = "old title"
        commonData.expirationDate = Timestamp.valueOf(OffsetDateTime.of(2020, 12, 9, 0, 0, 0, 0, ZoneOffset.ofHours(0)).toLocalDateTime())
        commonData.owner = "owner cat rec id"
        commonData.smu = 10
        commonData.recommendationDetails = new ArrayList<>()
        commonData.recommendationAudits = new ArrayList<>()
        return commonData
    }

    def buildTemplateFields() {
        def templateFields = new ArrayList()
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "title"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "smu"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "recommendationNumber"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "recommendationOwner"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "expirationTime"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "assetId"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "customer"
        ))
        templateFields.add(new RecommendationFieldsEntity(
                isCommonHeaderField: true,
                fieldName: "site"
        ))

        return templateFields
    }

    def buildPutRequestMinimalData() {
        def payload = new RecommendationPutRequest()
        payload.title = "old title"
        payload.expirationTime = OffsetDateTime.of(2020, 12, 9, 0, 0, 0, 0, ZoneOffset.ofHours(0))
        payload.owner = new BaseOwner(catrecid: "owner cat rec id")
        payload.hoursReading = new HoursReading(reading: 10, unitOfMeasure: "hours")
        payload.templateCustomProperties = new ArrayList<>()

        return payload
    }
}
