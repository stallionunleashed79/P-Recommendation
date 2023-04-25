package com.cat.digital.reco.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents technical details about the error with additional message.
 */
@ApiModel(description = "Represents technical details about the error with additional message.")
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalInfo {

  @JsonProperty("field")
  private String field;

  @JsonProperty("message")
  private String message;

  @JsonProperty("subCode")
  private String subCode;

}

