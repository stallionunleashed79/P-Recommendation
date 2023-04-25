package com.cat.digital.reco.domain.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Recommendation Export Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationCustomOptionData {

  @JsonProperty("customer")
  @ApiModelProperty(example = "Waste Management", value = "Recommendation template")
  public String customer;

  @JsonProperty("valueEstimateCurrency")
  @ApiModelProperty(example = "US Dollar", value = "Currency")
  public String valueEstimateCurrency;

  @JsonProperty("valueEstimateFailureCost")
  @ApiModelProperty(example = "100.00", value = "Estimated Failure Cost")
  public String valueEstimateFailureCost;

  @JsonProperty("valueEstimateRepairCost")
  @ApiModelProperty(example = "300.00", value = "Estimated Repair Cost")
  public String valueEstimateRepairCost;

}
