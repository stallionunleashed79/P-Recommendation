package com.cat.digital.reco.domain.models;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

import com.cat.digital.reco.validators.interfaces.IExpirationDateValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "The common fields part of the PUT Recommendation request body")
@Data
public class PutCommonFields extends CommonFields {
  @JsonProperty("title")
  @ApiModelProperty(example = "Draft Recommendation", required = true, value = "Title of the recommendation")
  @NotBlank(message = "Title is required")
  @Size(max = 200, message = "Title must be less than 200 characters")
  private String title;

  @JsonProperty("expirationTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", required = true, value = "When the recommendation should expire. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @NotNull(message = "Expiration time is required")
  @Future(message = "The Expiration Time must be within one year from today")
  @IExpirationDateValidator
  private OffsetDateTime expirationTime;
}

