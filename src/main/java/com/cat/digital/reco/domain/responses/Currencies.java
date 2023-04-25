package com.cat.digital.reco.domain.responses;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Currencies")
@Data
public class Currencies {
  @JsonProperty("currencyCode")
  @ApiModelProperty(example = "USD", required = true, value = "Three leter code of the currency - ISO-4217")
  @NotNull
  private String currencyCode;

  @JsonProperty("currencyName")
  @ApiModelProperty(example = "US Dollar", required = true, value = "Full name of the currency")
  @NotNull
  private String currencyName;
}

