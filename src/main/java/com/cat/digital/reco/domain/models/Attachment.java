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

@ApiModel(description = "Attachments associated with the recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attachment {

  @JsonProperty("name")
  @ApiModelProperty(example = "File Name", required = true, value = "Name of the attachment")
  @NotNull
  private String name;

  @JsonProperty("fileId")
  @ApiModelProperty(example = "8e84cf3d-b815-40d5-9750-1e4784fc5b75", required = true, value = "File Id of the attachment")
  @NotNull
  private String fileId;

  @JsonProperty("fileAttachedTime")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @ApiModelProperty(example = "2016-01-31T22:59:59.999Z", value = "When the file was attached to the recommendation. Formatted string that represents date, time and timezone offset in ISO-8601 format. For more information see https://tools.ietf.org/html/rfc3339. ")
  @Pattern(regexp = "^\\d{4}-\\d\\d-\\d\\d[Tt]\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|[Zz])?$")
  private OffsetDateTime fileAttachedTime;
}

