package com.cat.digital.reco.service.impl;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cat.digital.reco.domain.entities.RecommendationAuditsEntity;
import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity;
import com.cat.digital.reco.domain.entities.RecommendationCustomDataEntity;
import com.cat.digital.reco.domain.entities.RecommendationFieldsEntity;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
import com.cat.digital.reco.repositories.RecommendationAuditRepository;
import com.cat.digital.reco.service.AuditService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditServiceImpl implements AuditService {

  String defaultTimeZone;

  RecommendationAuditRepository auditRepository;

  public AuditServiceImpl(@Value("${cat.timezone.default:UTC}") String defaultTimeZone, RecommendationAuditRepository auditRepository) {
    this.defaultTimeZone = defaultTimeZone;
    this.auditRepository = auditRepository;
  }

  public void setAuditFields(RecommendationPutRequest payload, RecommendationCommonDataEntity previousRecommendationData, List<RecommendationFieldsEntity> templateFields) {
    templateFields = templateFields.stream().filter(RecommendationFieldsEntity::isCommonHeaderField).collect(Collectors.toList());

    var storedDataWithCommonData = new ArrayList<>(previousRecommendationData.getRecommendationDetails());
    var payloadWithCommonData = new ArrayList<>(payload.getTemplateCustomProperties());

    fillCustomRecommendationDataWithCommonData(storedDataWithCommonData, previousRecommendationData, templateFields);
    fillCustomPropertiesWithCommonProperties(payloadWithCommonData, payload);

    var modifiedDate = Timestamp.from(ZonedDateTime.now(ZoneId.of(defaultTimeZone)).toInstant());

    var payloadCustomFieldMap = payloadWithCommonData.stream().collect(Collectors.toMap(TemplateCustomField::getPropertyName, TemplateCustomField::getPropertyValue));

    var audits = auditRepository.listLastsUpdates(previousRecommendationData.getRecommendationId());
    var auditFieldNames = audits.stream().map(audit -> audit.getRecommendationField().getFieldName()).collect(Collectors.toList());

    /*
     * get data existed on all list, payload, custom data table and audit table
     * filtered only the fields exist on all list and have a different value
     */

    var filteredAuditData = audits.stream().filter(audit ->
        !payloadCustomFieldMap.containsKey(audit.getRecommendationField().getFieldName()) || !payloadCustomFieldMap.get(audit.getRecommendationField().getFieldName()).equals(audit.getNewValue())
    ).collect(Collectors.toList());

    /*
     * create audits of the previous list,
     * if the field isn't present on custom data table, we will set a default value to symbolize the empty value
     */
    filteredAuditData = filteredAuditData.stream().map(audit -> RecommendationAuditsEntity.builder()
        .recommendationId(audit.getRecommendationId())
        .recommendationFieldId(audit.getRecommendationFieldId())
        .recommendationField(audit.getRecommendationField())
        .id(audit.getId() + 1)
        .oldValue(audit.getNewValue())
        .newValue(
            payloadCustomFieldMap.getOrDefault(audit.getRecommendationField().getFieldName(), "")
        )
        .modifyBy(previousRecommendationData.getUpdatedByCatrecid())
        .modifiedDate(modifiedDate)
        .build())
        .collect(Collectors.toList());

    /*
     * create the new audits for a fields wasn't previously modified
     */
    filteredAuditData.addAll(storedDataWithCommonData.stream().filter(data -> !auditFieldNames.contains(data.getRecommendationField().getFieldName()) && !payloadCustomFieldMap.getOrDefault(data.getRecommendationField().getFieldName(), "").equals(data.getValue())).map(data -> RecommendationAuditsEntity.builder()
        .recommendationId(data.getRecommendationId())
        .recommendationFieldId(data.getFieldId())
        .recommendationField(data.getRecommendationField())
        .id(1)
        .oldValue(data.getValue())
        .newValue(payloadCustomFieldMap.getOrDefault(data.getRecommendationField().getFieldName(), ""))
        .modifyBy(previousRecommendationData.getUpdatedByCatrecid())
        .modifiedDate(modifiedDate)
        .build())
        .collect(Collectors.toList()));

    previousRecommendationData.setRecommendationAudits(filteredAuditData);

    // log fields audited
    filteredAuditData.forEach(audit ->
        log.debug("field name modified {}, old value {} new value {} modified by {} in the date {}",
            audit.getRecommendationField().getFieldName(),
            audit.getOldValue(),
            audit.getNewValue(),
            audit.getModifyBy(),
            audit.getModifiedDate()
        )
    );
  }

  private void fillCustomRecommendationDataWithCommonData(List<RecommendationCustomDataEntity> recommendationCustomDataList, RecommendationCommonDataEntity recommendationData, List<RecommendationFieldsEntity> commonFields) {
    var commonFieldsMaps = commonFields.stream().collect(Collectors.toMap(RecommendationFieldsEntity::getFieldName, f -> f));

    var titleField = commonFieldsMaps.get(CommonDataEnum.TITLE.value);
    var value = recommendationData.getTitle();
    if (!Objects.isNull(recommendationData.getTitle())) {
      recommendationCustomDataList.add(RecommendationCustomDataEntity.builder()
          .recommendationId(recommendationData.getRecommendationId())
          .fieldId(titleField.getFieldId())
          .recommendationField(titleField)
          .value(value)
          .build()
      );
    }

    titleField = commonFieldsMaps.get(CommonDataEnum.SMU.value);
    value = String.valueOf(recommendationData.getSmu());
    recommendationCustomDataList.add(RecommendationCustomDataEntity.builder()
        .recommendationId(recommendationData.getRecommendationId())
        .fieldId(titleField.getFieldId())
        .recommendationField(titleField)
        .value(value)
        .build()
    );

    titleField = commonFieldsMaps.get(CommonDataEnum.RECOMMENDATION_OWNER.value);
    value = recommendationData.getOwner();
    recommendationCustomDataList.add(RecommendationCustomDataEntity.builder()
        .recommendationId(recommendationData.getRecommendationId())
        .fieldId(titleField.getFieldId())
        .recommendationField(titleField)
        .value(value)
        .build()
    );

    titleField = commonFieldsMaps.get(CommonDataEnum.EXPIRATION_TIME.value);
    value = OffsetDateTime.of(recommendationData.getExpirationDate().toLocalDateTime(), ZoneOffset.UTC).toString();
    recommendationCustomDataList.add(RecommendationCustomDataEntity.builder()
        .recommendationId(recommendationData.getRecommendationId())
        .fieldId(titleField.getFieldId())
        .recommendationField(titleField)
        .value(value)
        .build()
    );
  }

  private void fillCustomPropertiesWithCommonProperties(List<TemplateCustomField> commonProperties, RecommendationPutRequest payload) {


    if (!Objects.isNull(payload.getTitle())) {
      commonProperties.add(
          TemplateCustomField.builder()
              .propertyName(CommonDataEnum.TITLE.getValue())
              .propertyValue(payload.getTitle())
              .build()
      );
    }

    if (!Objects.isNull(payload.getHoursReading().getReading())) {
      commonProperties.add(
          TemplateCustomField.builder()
              .propertyName(CommonDataEnum.SMU.getValue())
              .propertyValue(payload.getHoursReading().getReading().toString())
              .build()
      );
    }

    if (!Objects.isNull(payload.getOwner().getCatrecid())) {
      commonProperties.add(
          TemplateCustomField.builder()
              .propertyName(CommonDataEnum.RECOMMENDATION_OWNER.getValue())
              .propertyValue(payload.getOwner().getCatrecid())
              .build()
      );
    }

    if (!Objects.isNull(payload.getExpirationTime())) {
      commonProperties.add(
          TemplateCustomField.builder()
              .propertyName(CommonDataEnum.EXPIRATION_TIME.getValue())
              .propertyValue(payload.getExpirationTime().toString())
              .build()
      );
    }
  }

  @AllArgsConstructor
  @Getter
  private enum CommonDataEnum {
    TITLE("title"),
    SMU("smu"),
    RECOMMENDATION_NUMBER("recommendationNumber"),
    RECOMMENDATION_OWNER("recommendationOwner"),
    EXPIRATION_TIME("expirationTime"),
    ASSET_ID("assetId"),
    CUSTOMER("customer"),
    SITE("site");

    private final String value;
  }
}