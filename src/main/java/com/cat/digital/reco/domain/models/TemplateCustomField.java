package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Property of a request that changed with template definition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateCustomField {
  @JsonProperty("propertyName")
  @ApiModelProperty(example = "priority", required = true, value = "Name of the property")
  @NotNull(message = "propertyName is required")
  private String propertyName;

  @JsonProperty("propertyValue")
  @ApiModelProperty(example = "3 - At Next Service", required = true, value = "Value of the property")
  @NotNull(message = "propertyValue is required")
  @JacksonXmlCData
  private String propertyValue;
}

