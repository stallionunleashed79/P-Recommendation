package com.cat.digital.reco.domain.responses;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Recommendation Export Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationExport {
  private String createdDate;
  private String owner;
  private String model;
  private String serialNumber;
  private String assetName;
  private String ucidName;
  private String recommendationNumber;
  private String title;
  private String recommendationPriority;
  private String recommendationStatus;
  private String site;
  private String updatedDate;
  private String expirationDate;
  private String currency;
  private String estimatedFailureCost;
  private String repairCost;
}
