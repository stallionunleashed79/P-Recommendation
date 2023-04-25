package com.cat.digital.reco.domain.responses;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendations {
  private List<Recommendation> recommendations;

  private ResponseMetadata responseMetadata;
}
