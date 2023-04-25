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

@ApiModel(description = "User Object")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
  @JsonProperty("catrecid")
  @ApiModelProperty(example = "AAA-98765432", required = true, value = "The catrecid of the recommendation user")
  @NotNull
  private String catrecid;

  @JsonProperty("firstName")
  @ApiModelProperty(example = "John", value = "First name of user")
  private String firstName;

  @JsonProperty("lastName")
  @ApiModelProperty(example = "Smith", value = "Last name of User")
  private String lastName;
}

