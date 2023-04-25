package com.cat.digital.reco.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendationField {
  RECOMMENDATION_PRIORITY("recommendationPriority"),
  RECOMMENDATION_STATUS("recommendationStatus"),
  RECOMMENDATION_NUMBER("recommendationNumber"),
  EXPIRATION_TIME("expirationTime"),
  CUSTOMER("customer"),
  VALUE_ESTIMATE_CURRENCY("valueEstimateCurrency"),
  VALUE_ESTIMATE_REPAIR_COST("valueEstimateRepairCost"),
  VALUE_ESTIMATE_FAILURE_COST("valueEstimateFailureCost"),
  VALUE_ESTIMATE_RECOMMENDATION_VALUE("valueEstimateRecommendationValue"),
  WORK_ORDER_ID("workOrderId"),
  DEALER_CODE("dealerCode"),
  SERIAL_NUMBER("serialNumber"),
  TITLE("title"),
  ASSET_NAME("assetName"),
  SITE("site"),
  CUSTOMER_UCID("customerUcid"),
  CREATOR_NAME("createdByCatrecid"),
  OWNER("owner"),
  IS_DEALER_RECOMMENDATION("dealerRecommendation");
  private final String value;
}
