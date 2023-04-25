package com.cat.digital.reco.domain.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Base model of all the containers types allowed in the template definition.
 */
@Data
@Builder
public class ContainerType   {

  @JsonProperty("type")
  private TypeEnum type;

  /**
   * Available containers types in the template.
   */
  @Getter
  @AllArgsConstructor
  public enum TypeEnum {
    ACCORDION("Accordion"),
    FORM("Form"),
    GRID("Grid"),
    TABLE("Table"),
    FRAGMENT_FORM_3("Fragment-Form-3"),
    ACCORDION_FORM_1("Accordion-Form-1"),
    ACCORDION_FORM_3("Accordion-Form-3"),
    ACCORDION_LINKSATTACHMENTS("Accordion-LinksAttachments"),
    ACCORDION_TABLE_EXCEPTION("Accordion-Table-Exception"),
    ACCORDION_TABLE_EVENT("Accordion-Table-Event");

    private final String value;

    public static TypeEnum fromValue(final String value) {
      return Arrays.stream(TypeEnum.values()).filter(type ->
          type.value.equals(value)).findAny().orElseThrow(() -> new IllegalArgumentException(String.format("Unexpected value '%s'", value)));
    }
  }

}

