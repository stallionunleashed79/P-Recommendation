package com.cat.digital.reco.domain.responses;

import javax.validation.Valid;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MetadataResponseFiles {
  @JsonProperty("fileName")
  @ApiModelProperty(example = "report.xls", value = "File name")
  private String fileName;

  @JsonProperty("contentLength")
  @ApiModelProperty(example = "1500", value = "Actual content size of file")
  @Valid
  private BigDecimal contentLength;

  @JsonProperty("url")
  @ApiModelProperty(example = "https://domain.com/fileName", value = "File URL of the attachment")
  private String url;

  @JsonProperty("status")
  private String status;

  @JsonProperty("includedInEmail")
  @ApiModelProperty(example = "uploaded", value = "status of file")
  private Boolean includedInEmail;
}

