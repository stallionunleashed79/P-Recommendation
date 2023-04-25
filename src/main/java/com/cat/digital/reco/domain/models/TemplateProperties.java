package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Template properties information.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateProperties   {
  @JsonProperty("sectionName")
  @NotNull
  private String sectionName;

  @JsonProperty("sectionPosition")
  @NotNull
  private Integer sectionPosition;

  @JsonProperty("sectionContainerType")
  @NotNull
  @Valid
  private ContainerType sectionContainerType;

  @JsonProperty("sectionProperties")
  @Valid
  private List<TemplateSectionProperty> sectionProperties = null;
}

