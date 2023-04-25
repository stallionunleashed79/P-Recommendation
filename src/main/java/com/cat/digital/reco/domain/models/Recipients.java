package com.cat.digital.reco.domain.models;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;

import com.cat.digital.reco.validators.email.ConditionalEmail;
import com.cat.digital.reco.validators.email.ExtendedEmail;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;


@Data
@ApiModel(description = "Recipients for the recommendation. Allow multiple email address. At least one (to) recipient is required")

public class Recipients {

  @JsonProperty("to")
  @ApiModelProperty(example = "[\"user.name@email.com\",\"userto.name@email.com\"]", required = true, value = "")
  @Size(min = 1, message = "At least one email address is required")
  @ExtendedEmail(message = "Email must be a valid email address")
  private List<String> to = new ArrayList<>();

  @JsonProperty("cc")
  @ApiModelProperty(example = "[\"user.name2@email.com\"]", value = "")
  @ConditionalEmail(message = "Email must be a valid email address")
  private List<String> cc;

  @JsonProperty("bcc")
  @ApiModelProperty(example = "[\"user.name3@email.com\"]", value = "")
  @ConditionalEmail(message = "Email must be a valid email address")
  private List<String> bcc;
}

