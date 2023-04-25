package com.cat.digital.reco.domain.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "The common fields part of the Recommendation response details body")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonFieldsResponse {
  @JsonProperty("title")
  @ApiModelProperty(example = "Draft Recommendation", required = true, value = "Title of the recommendation")
  @NotNull
  private String title;

  @JsonProperty("expirationTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", required = true, value = "When the recommendation should expire. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @NotNull
  @Valid
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime expirationTime;

  @JsonProperty("createdTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the recommendation was created. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Valid
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime createdTime;

  @JsonProperty("updatedTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the recommendation was last updated. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Valid
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime updatedTime;

  @JsonProperty("hoursReading")
  private HoursReading hoursReading;

  @JsonProperty("site")
  private String site;


  @JsonProperty("owner")
  @ApiModelProperty(value = "The user information for the owner of the recommendation")
  @Valid
  private User owner = null;

  @JsonProperty("createdBy")
  @ApiModelProperty(value = "The user information for the creator of the recommendation")
  @Valid
  private User createdBy = null;

  @JsonProperty("updatedBy")
  @ApiModelProperty(value = "The user information of who last updated the recommendation")
  @Valid
  private User updatedBy = null;

  @JsonProperty("assetOwnershipAtRecommendation")
  @ApiModelProperty(value = "")
  @Valid
  private AssetOwnershipAtRecommendation assetOwnershipAtRecommendation;

  @JsonProperty("assetMetadata")
  @ApiModelProperty(value = "")
  @Valid
  private AssetMetaData assetMetadata;
}

