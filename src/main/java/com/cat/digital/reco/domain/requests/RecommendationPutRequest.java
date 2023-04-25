package com.cat.digital.reco.domain.requests;

import java.util.List;

import com.cat.digital.reco.domain.models.PutCommonFields;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Request body to update a recommendation")
@Data
public class RecommendationPutRequest extends PutCommonFields {

  @JsonProperty("templateCustomProperties")
  @ApiModelProperty(value = "")
  private List<TemplateCustomField> templateCustomProperties = null;

}

