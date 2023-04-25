package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Template name and description.
 */
@Data
@Builder
public class TemplateInformation   {
  @JsonProperty("name")
  @NotNull
  private String name;

  @JsonProperty("description")
  @NotNull
  private String description;
}

