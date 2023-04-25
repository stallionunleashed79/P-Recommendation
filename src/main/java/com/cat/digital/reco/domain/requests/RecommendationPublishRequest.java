package com.cat.digital.reco.domain.requests;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cat.digital.reco.domain.models.Recipients;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Request body to publish/send a recommendation")
public class RecommendationPublishRequest {

  @JsonProperty("recipients")
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  private Recipients recipients;

  @JsonProperty("subject")
  @ApiModelProperty(example = "Title of recommendation", required = true, value = "Subject for the recommendation.")
  @Size(min = 10, max = 200, message = "The length should be have between 10 and 100 characters.")
  @NotNull(message = "The subject is required.")
  @NotEmpty(message = "The subject is required.")
  private String subject;

  @JsonProperty("message")
  @ApiModelProperty(example = "Dealer please apply the recommendation actions added in the PDF file", value = "Email content for the recommendation.")
  @Size(min = 0, max = 2000, message = "The length should be have between 0 and 2000 characters.")
  private String message;

  @JsonProperty("sendCopyToSender")
  @ApiModelProperty(example = "true", required = true, value = "True - Indicates the sender want to have a copy of email.")
  @NotNull(message = "The sendCopyToSender is required. Allowed input: true or false")
  private Boolean sendCopyToSender;

}

