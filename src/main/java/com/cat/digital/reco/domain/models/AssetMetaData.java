package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Asset Meta Data of the asset associated with the recommendation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetMetaData {
  @JsonProperty("serialNumber")
  @ApiModelProperty(example = "2WS23456", required = true, value = "Serial Number of asset associated with recommendation")
  @NotNull
  private String serialNumber;

  @JsonProperty("make")
  @ApiModelProperty(example = "CAT", required = true, value = "Make of asset associated with recommendation")
  @NotNull
  private String make;

  @JsonProperty("model")
  @ApiModelProperty(example = "123AB", required = true, value = "Model of asset associated with recommendation")
  @NotNull
  private String model;

  @JsonProperty("name")
  @ApiModelProperty(example = "CAT|RXZ00353|2969412354", required = true, value = "Asset identifier code")
  @NotNull
  private String name;

  @JsonProperty("iconUrl")
  @ApiModelProperty(example = "https://services.cat.com/images/wheelloader.png", required = true, value = "Url of the icon")
  @NotNull
  private String iconUrl;

  @JsonProperty("productFamily")
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  private AssetMetaDataProductFamily productFamily;
}

