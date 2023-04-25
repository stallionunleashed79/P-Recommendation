package com.cat.digital.reco.domain.responses;

import javax.validation.Valid;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "List of currencies")
@Data
public class RecommendationCurrency {
  @JsonProperty("currencies")
  @ApiModelProperty(value = "")
  @Valid
  private List<Currencies> currencies = null;
}

