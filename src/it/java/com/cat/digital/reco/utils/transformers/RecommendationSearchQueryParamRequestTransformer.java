package com.cat.digital.reco.utils.transformers;

import com.cat.digital.reco.model.RecommendationSearchQueryParam;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.datatable.DataTableType;

import java.util.Map;

import static com.cat.digital.reco.common.Constants.*;

public class RecommendationSearchQueryParamRequestTransformer {
  public static void transform(TypeRegistry typeRegistry) {
    typeRegistry.defineDataTableType(new DataTableType(RecommendationSearchQueryParam.class, (Map<String, String> row) -> {

          RecommendationSearchQueryParam recommendationSearchQueryParam = new RecommendationSearchQueryParam();
          recommendationSearchQueryParam.setSortBy(row.get(SORT_BY));
          recommendationSearchQueryParam.setOrderBy(row.get(ORDER_BY));
          recommendationSearchQueryParam.setCursor(row.get(CURSOR));
          recommendationSearchQueryParam.setLimit(row.get(LIMIT));
          recommendationSearchQueryParam.setSkip(row.get(SKIP));
          recommendationSearchQueryParam.setSearchValue(SEARCH_VALUE);

          return recommendationSearchQueryParam;
        }
        )
    );
  }
}
