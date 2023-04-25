package com.cat.digital.reco.domain.responses;

import javax.validation.constraints.NotNull;
import java.util.List;

import com.cat.digital.reco.domain.models.CommonFieldsResponse;
import com.cat.digital.reco.domain.models.Attachment;
import com.cat.digital.reco.domain.models.RecommendationLink;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Response body with all recommendation data details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationDetailsResponse {
  @JsonProperty("recommendationNumber")
  @ApiModelProperty(example = "REC 123-456-789", value = "System generated number to identify an Recommendation.")
  private String recommendationNumber;

  @JsonProperty("assetId")
  @ApiModelProperty(example = "CAT|RXZ00353|2969412354", required = true, value = "Asset identifier code")
  @NotNull
  private String assetId;

  @JsonProperty("templateName")
  @ApiModelProperty(example = "Default template", value = "Recommendation template")
  private String templateName;

  @JsonProperty("commonFields")
  private CommonFieldsResponse commonFields;

  @JsonProperty("templateCustomProperties")
  private List<TemplateCustomField> templateCustomProperties = null;

  @JsonProperty("attachments")
  @ApiModelProperty(value = "List of attachments associated with the recommendation.")
  private List<Attachment> attachments = null;


  @JsonProperty("links")
  @ApiModelProperty(value = "List of links associated with the recommendation. Requires ReferenceId as a parameter.")
  private List<RecommendationLink> links = null;

  @JsonProperty("exceptions")
  @ApiModelProperty(example = "https://services.cat.com/catDigital/conditionMonitoring/v1/{referenceID}", value = "URL for the exceptions associated with the recommendation.")
  private String exceptions;

  @JsonProperty("events")
  @ApiModelProperty(example = "https://services.cat.com/catDigital/conditionMonitoring/v1/events", value = "URL for the events associated with the recommendation. Can filter based on id as a parameter")
  private String events;
}

