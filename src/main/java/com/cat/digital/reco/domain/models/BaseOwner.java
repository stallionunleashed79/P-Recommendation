package com.cat.digital.reco.domain.models;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@ApiModel(description = "The owner of the recommendation")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseOwner {

  @JsonProperty("catrecid")
  @ApiModelProperty(example = "AAA-98765432", required = true, value = "The catericid of the recommendation owner, the value will be extracted from the logged in user if not provided")
  @NotBlank(message = "Owner catrecid is required")
  private String catrecid;

}

