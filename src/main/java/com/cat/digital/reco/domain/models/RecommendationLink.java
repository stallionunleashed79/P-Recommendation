package com.cat.digital.reco.domain.models;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Links associated with the recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationLink {

  @JsonProperty("noteId")
  @NotNull
  private String noteId;

  @JsonProperty("attachedDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the link was attached to the recommendation. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime attachedTime;

  @JsonProperty("url")
  @ApiModelProperty(example = "https://example.com", required = true, value = "URL of the link.")
  @NotNull
  private String url;
}

