package com.cat.digital.reco.domain.requests;

import com.cat.digital.reco.domain.models.ResponseAttribute;
import com.cat.digital.reco.filter.FilterableRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Request body to search for recommendations")
public class RecommendationSearchRequest extends FilterableRequest<ResponseAttribute> {
}

