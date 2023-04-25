package com.cat.digital.reco.domain.models;

/**
 * List of recommendation search sorting options.
 */
public enum RecommendationSortByParam {
  SERIAL_NUMBER("serialNumber", "recommendationCommonData.serialNumber"),
  MAKE("make", "recommendationCommonData.make"),
  MODEL("model", "recommendationCommonData.model"),
  CREATED_TIME("createdTime", "recommendationCommonData.createdDate"),
  TITLE("title", "recommendationCommonData.title"),
  RECOMMENDATION_PRIORITY("recommendationPriority", "recommendationSort.recommendationPriority"),
  RECOMMENDATION_STATUS("recommendationStatus", "recommendationSort.recommendationStatus"),
  EXPIRATION_TIME("expirationTime", "recommendationCommonData.expirationDate"),
  UCID_NAME("ucidName", "recommendationCommonData.customerUcid"),
  SITE("site", "recommendationCommonData.site"),
  ASSET_ID("assetId", "recommendationCommonData.assetName"),
  OWNER("owner", "recommendationCommonData.ownedBy.catrecid"),
  RECOMMENDATION_NUMBER("recommendationNumber", "recommendationNumber"),
  TEMPLATE_NAME("templateName", "recommendationCommonData.template.templateName"),
  WORK_ORDER_ID("workOrderId", "recommendationSort.workOrderNumber"),
  NUMBER_OF_DAYS_SINCE_MODIFIED("numberOfDaysSinceModified", "recommendationCommonData.updatedDate");

  private final String sortName;
  private final String value;

  RecommendationSortByParam(String name, String value) {
    this.sortName = name;
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public String getSortName() {
    return this.sortName;
  }

  public static RecommendationSortByParam findByName(final String name) {
    for (RecommendationSortByParam value : values()) {
      if (value.getSortName().equals(name)) {
        return value;
      }
    }
    return null;
  }

}
