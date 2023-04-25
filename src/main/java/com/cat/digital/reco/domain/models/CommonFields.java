package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * The common fields part of the recommendation request body.
 */
@ApiModel(description = "The common fields part of the recommendation request body.")
@Data
public class CommonFields   {

  @JsonProperty("owner")
  @ApiModelProperty(required = true, value = "")
  @NotNull(message = "The Recommendation must have an owner.")
  @Valid
  private BaseOwner owner;

  @JsonProperty("hoursReading")
  private HoursReading hoursReading;

  @JsonProperty("site")
  @ApiModelProperty(example = "Asset location", value = "Location alias name to identify group of assets located at a mine site or construction site.")
  private String site;

}

