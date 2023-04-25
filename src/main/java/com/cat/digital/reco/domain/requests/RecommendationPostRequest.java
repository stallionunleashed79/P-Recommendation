package com.cat.digital.reco.domain.requests;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import com.cat.digital.reco.domain.models.PostCommonFields;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Request body to create a recommendation")
public class RecommendationPostRequest extends PostCommonFields {

  @JsonProperty("assetId")
  @ApiModelProperty(example = "CAT|RXZ00353|2969412354", required = true, value = "Asset identifier code")
  @NotBlank(message = "AssetId is required")
  private String assetId;

  @JsonProperty("templateName")
  @ApiModelProperty(example = "Default template", value = "Recommendation template")
  @NotBlank(message = "Template name is required")
  private String templateName;

  @JsonProperty("templateCustomProperties")
  @ApiModelProperty(value = "")
  @Valid
  private List<TemplateCustomField> templateCustomProperties = null;
}

