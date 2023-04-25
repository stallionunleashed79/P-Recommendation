package com.cat.digital.reco.domain.models;

public enum RecommendationExportFieldName {
  CREATEDDATE("Created Date"),
  OWNER("Owner"),
  MODEL("Model"),
  SERIALNUMBER("Serial Number"),
  ASSETNAME("Asset ID"),
  UCIDNAME("Customer"),
  RECOMMENDATIONNUMBER("Recommendation Number"),
  TITLE("Title"),
  RECOMMENDATIONPRIORITY("Priority"),
  RECOMMENDATIONSTATUS("Status"),
  SITE("Site"),
  UPDATEDDATE("Last Updated Date"),
  EXPIRATIONDATE("Expiration Date"),
  CURRENCY("Currency"),
  ESTIMATEDFAILURECOST("Estimated Failure Cost"),
  REPAIRCOST("Repair Cost");

  private String value;

  RecommendationExportFieldName(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}