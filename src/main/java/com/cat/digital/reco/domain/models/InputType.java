package com.cat.digital.reco.domain.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Base model for all input types allowed in the template definition.
 */
@Data
@Builder
public class InputType   {

  @JsonProperty("type")
  private TypeEnum type;

  /**
   * Available input types in the template.
   */
  @Getter
  @AllArgsConstructor
  public enum TypeEnum {
    TEXT("text"),
    TEXTAREA("textarea"),
    DROPDOWN("dropdown"),
    CHECKBOX("checkbox"),
    BUTTON("button"),
    LOOKUP("lookup"),
    DATE("date"),
    SECTION_HEADER_PILL_RIGHT("section-header-pill-right"),
    SECTION_FOOTER_LABEL_RIGHT("section-footer-label-right"),
    SECTION_FOOTER_LABEL_LEFT("section-footer-label-left");

    private final String value;

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      return Arrays.stream(TypeEnum.values()).filter(type -> type.value.equals(value)).findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Unexpected value '%s'", value)));
    }
  }
}

