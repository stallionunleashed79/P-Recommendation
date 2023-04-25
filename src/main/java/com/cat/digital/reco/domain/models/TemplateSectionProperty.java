package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Template section properties.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateSectionProperty   {
  @JsonProperty("propertyName")
  @NotNull
  private String propertyName;

  @JsonProperty("displayName")
  @NotNull
  private String displayName;

  @JsonProperty("dataType")
  @NotNull
  @Valid
  private DataType dataType;

  @JsonProperty("inputType")
  @NotNull
  @Valid
  private InputType inputType;

  @JsonProperty("isRequired")
  @NotNull
  private Boolean isRequired;

  @JsonProperty("isReadOnly")
  @NotNull
  private Boolean isReadOnly;

  @JsonProperty("position")
  @NotNull
  private Integer position;

  @JsonProperty("defaultValue")
  private String defaultValue;

  @JsonProperty("minimumPropertyLength")
  private Integer minimumPropertyLength;

  @JsonProperty("maximumPropertyLength")
  private Integer maximumPropertyLength;

  @JsonProperty("minimumPropertyValue")
  private String minimumPropertyValue;

  @JsonProperty("maximumPropertyValue")
  private String maximumPropertyValue;

  @JsonProperty("propertyOptions")
  @Valid
  private List<PropertyOption> propertyOptions;
}

