package com.cat.digital.reco.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@ApiModel(description = "The owner of the recommendation")
@Data
@SuperBuilder
public class Owner extends BaseOwner {
  @JsonProperty("firstName")
  @ApiModelProperty(example = "John", required = true, value = "The first name of recommendations owner")
  private String firstName;

  @JsonProperty("lastName")
  @ApiModelProperty(example = "Smith", required = true, value = "The last name of recommendations owner")
  private String lastName;
}

