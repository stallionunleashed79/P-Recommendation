package com.cat.digital.reco.domain.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Base model for all data types allowed in the template definition.
 */
@Data
@Builder
public class DataType   {


  @JsonProperty("type")
  private TypeEnum type;

  /**
   * Available data types in the template.
   */
  @Getter
  @AllArgsConstructor
  public enum TypeEnum {
    STRING("String"),
    INTEGER("Integer"),
    DOUBLE("Double"),
    BOOLEAN("Boolean"),
    TIMESTAMP("Timestamp"),
    USER("User");

    private final String value;

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      return Arrays.stream(TypeEnum.values()).filter(type -> type.value.equals(value)).findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Unexpected value '%s'", value)));
    }
  }
}

