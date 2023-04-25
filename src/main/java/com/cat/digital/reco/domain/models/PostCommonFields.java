package com.cat.digital.reco.domain.models;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

import com.cat.digital.reco.validators.interfaces.IExpirationDateValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "The common fields part of the POST Recommendation request body")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostCommonFields extends CommonFields {
  @JsonProperty("title")
  @ApiModelProperty(example = "Draft Recommendation", value = "Title of the recommendation")
  @Size(max = 100, message = "Title cannot exceed 100 characters")
  private String title;

  @JsonProperty("expirationTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the recommendation should expire. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Future(message = "Expiration time cannot be in the past")
  @IExpirationDateValidator
  private OffsetDateTime expirationTime;
}

