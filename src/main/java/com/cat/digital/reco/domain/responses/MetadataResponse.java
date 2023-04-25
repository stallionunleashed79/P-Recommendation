package com.cat.digital.reco.domain.responses;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Response of the recommendation metadata")
@Data
public class MetadataResponse {
  @JsonProperty("recommendationNumber")
  @ApiModelProperty(example = "REC 123-456-789", value = "System generated number to identify an Recommendation.")
  private String recommendationNumber;

  @JsonProperty("totalAttachmentSize")
  @ApiModelProperty(example = "10.5", value = "The current attachments size for existing recommendations")
  @Valid
  private BigDecimal totalAttachmentSize;

  @JsonProperty("representUnit")
  @ApiModelProperty(example = "MB", value = "Unit of information or computer storage")
  private String representUnit;

  @JsonProperty("files")
  @ApiModelProperty(example = "[{\"fileName\":\"Report.xls\",\"contentLenght\":1500,\"status\":\"uploaded\",\"includedInEmail\":true,\"url\":\"https://domain.com/fileName\"},{\"fileName\":\"Actions.pdf\",\"contentLenght\":1000,\"status\":\"uploaded\",\"includedInEmail\":false,\"url\":\"https://domain.com/fileName2\"}]", value = "")
  @Valid
  private List<MetadataResponseFiles> files = null;
}

