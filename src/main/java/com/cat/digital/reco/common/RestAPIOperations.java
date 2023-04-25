package com.cat.digital.reco.common;

/**
 * An enum that maps the HTTP API operations to Entitlements API response operations.
 */
public enum RestAPIOperations {
  POST("create"),
  PUT("update"),
  DELETE("delete"),
  GET("view");

  private final String description;

  RestAPIOperations(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
