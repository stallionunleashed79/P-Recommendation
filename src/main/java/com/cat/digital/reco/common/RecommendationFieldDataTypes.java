package com.cat.digital.reco.common;

import java.util.Arrays;

import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.exceptions.UnsupportedDataTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing all the data types of recommendation fields.
 */
@Getter
@AllArgsConstructor
public enum RecommendationFieldDataTypes {
  STRING("String"),
  INTEGER("Integer"),
  DOUBLE("Double"),
  BOOLEAN("Boolean");

  private final String value;

  public static RecommendationFieldDataTypes getValue(final String value) {
    return Arrays.stream(RecommendationFieldDataTypes.values())
        .filter(type -> type.getValue().equalsIgnoreCase(value))
        .findAny()
        .orElseThrow(() -> new UnsupportedDataTypeException(
            String.format("data type %s not found on enum", value),
            CustomResponseCodes.INTERNAL_SERVER_ERROR));
  }
}
