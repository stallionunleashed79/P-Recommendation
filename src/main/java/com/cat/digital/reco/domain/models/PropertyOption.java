package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Based model of property options in the template.
 */
@Data
@Builder
public class PropertyOption   {
  @JsonProperty("name")
  @NotNull
  private String name;

  @JsonProperty("value")
  @NotNull
  private String value;
}

