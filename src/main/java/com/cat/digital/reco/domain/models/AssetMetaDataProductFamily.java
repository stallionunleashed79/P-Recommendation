package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetMetaDataProductFamily {
  @JsonProperty("code")
  @ApiModelProperty(example = "mwl", required = true, value = "Code for Product Family of Asset")
  @NotNull
  private String code;

  @JsonProperty("name")
  @ApiModelProperty(example = "Medium wheel loader", required = true, value = "Name for Product Family of Asset")
  @NotNull
  private String name;

  @JsonProperty("iconUrl")
  @ApiModelProperty(example = "https://services.cat.com/images/2WS23456.png", required = true, value = "Icon URL for Product Family of Asset")
  @NotNull
  private String iconUrl;
}

