package com.cat.digital.reco.domain.responses;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.cat.digital.reco.domain.models.Owner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recommendation {

  @JsonProperty("serialNumber")
  @ApiModelProperty(example = "CAT12345", required = true, value = "Serial Number of asset associated with recommendation")
  private String serialNumber;

  @JsonProperty("make")
  @ApiModelProperty(example = "CAT", required = true, value = "Make of asset associated with recommendation")
  private String make;

  @JsonProperty("model")
  @ApiModelProperty(example = "797F", required = true, value = "Model of asset associated with recommendation")
  private String model;

  @JsonProperty("createdTime")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the recommendation was created. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Valid
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime createdDate;

  @JsonProperty("title")
  @ApiModelProperty(example = "Draft Recommendation", required = true, value = "Title of the recommendation")
  private String title;

  @JsonProperty("recommendationPriority")
  @ApiModelProperty(example = "1 - Immediate Attention", required = true, value = "Recommendation Priority")
  private String recommendationPriority;

  @JsonProperty("recommendationStatus")
  @ApiModelProperty(example = "Draft", required = true, value = "Recommendation Status")
  private String recommendationStatus;

  @JsonProperty("expirationTime")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the recommendation was created. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Valid
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime expirationDate;

  @JsonProperty("ucidName")
  @ApiModelProperty(example = "Waste Management", required = true, value = "Customer  Name")
  private String ucidName;

  @JsonProperty("site")
  @ApiModelProperty(example = "Asset location", required = true, value = "Asset identifier code")
  private String site;

  @JsonProperty("assetId")
  @ApiModelProperty(example = "CAT|RXZ00353|2969412354", required = true, value = "Asset identifier code")
  private String assetName;

  @JsonProperty("owner")
  @ApiModelProperty(required = true, value = "Recommendation Owner")
  private Owner owner;

  @JsonProperty("recommendationNumber")
  @ApiModelProperty(example = "REC 123-456-789", value = "System generated number to identify an Recommendation.")
  private String recommendationNumber;

  @JsonProperty("templateName")
  @ApiModelProperty(example = "Default template", value = "Recommendation template")
  private String templateName;

  @JsonProperty("workOrderNumber")
  @ApiModelProperty(example = "T030-117279455AAKSN00209", value = "Work Order Number")
  private String workOrderNumber = "T030-117279455AAKSN00209";

}
