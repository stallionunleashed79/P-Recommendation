package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Asset owner information for the recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetOwnershipAtRecommendation {
  @JsonProperty("dealerCode")
  @ApiModelProperty(example = "TD00", value = "Code of the dealer")
  private String dealerCode;

  @JsonProperty("dealerName")
  @ApiModelProperty(example = "Holt", required = true, value = "Name of the dealer")
  @NotNull
  private String dealerName;

  @JsonProperty("ucid")
  @ApiModelProperty(example = "25669890", required = true, value = "UCID of the customer")
  @NotNull
  private String ucid;

  @JsonProperty("ucidName")
  @ApiModelProperty(example = "Waste Management", required = true, value = "Name of the customer")
  @NotNull
  private String ucidName;
}

