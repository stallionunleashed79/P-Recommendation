package com.cat.digital.reco.domain.models;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Recommendation Fields that are of Rich Text Format (RTF).
 */
@Getter
@AllArgsConstructor
public enum RecommendationRichTextFields {
  RECOMMENDATION_ACTION("recommendationAction"),
  RECOMMENDATION_DETAILS("recommendationDetails"),
  VALUE_ESTIMATE_DESCRIPTION("valueEstimateDescription");
  private String value;

  public static boolean isARichTextField(final String value) {
    return Arrays.asList(RecommendationRichTextFields.values()).stream().map(
        RecommendationRichTextFields::getValue).collect(Collectors.toList()).contains(value);
  }
}
