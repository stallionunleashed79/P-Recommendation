package com.cat.digital.reco.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Model to transform recommendation query parameter")
public class RecommendationSearchQueryParam {
    private String sortBy;
    private String orderBy;
    private String cursor;
    private String limit;
    private String skip;
    private String searchValue;
}