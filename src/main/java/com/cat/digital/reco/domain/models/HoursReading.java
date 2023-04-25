package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The Service Meter reading at the time of creating the recommendation.
 */
@ApiModel(description = "The Service Meter reading at the time of creating the recommendation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HoursReading {
  @JsonProperty("reading")
  @ApiModelProperty(example = "5000", required = true, value = "Service hours reading of the asset at the time of creating recommendation.")
  @NotNull(message = "Hours reading at recommendation is required")
  @Positive(message = "Hours reading at recommendation should be a positive value")
  private BigDecimal reading;

  @JsonProperty("unitOfMeasure")
  @ApiModelProperty(example = "hours", required = true, value = "The unit of measurement of the reading")
  @NotBlank(message = "Unit Of Measure is required")
  private String unitOfMeasure;
}

