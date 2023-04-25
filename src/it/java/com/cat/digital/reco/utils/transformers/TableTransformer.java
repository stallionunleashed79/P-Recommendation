package com.cat.digital.reco.utils.transformers;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;

import java.util.Locale;

public class TableTransformer implements TypeRegistryConfigurer {

    public Locale locale() {
        return Locale.ENGLISH;
    }

    private RecommendationSearchQueryParamRequestTransformer recommendationSearchQueryParamRequestTransformer = new RecommendationSearchQueryParamRequestTransformer();
    private RecommendationPostRequestTransformer recommendationPostRequestTransformer =
        RecommendationPostRequestTransformer.builder().build();
    private RecommendationPutRequestTransformer recommendationPutRequestTransformer =
        RecommendationPutRequestTransformer.builder().build();
    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        recommendationPostRequestTransformer.transform(typeRegistry);
        recommendationSearchQueryParamRequestTransformer.transform(typeRegistry);

        recommendationPutRequestTransformer.transform(typeRegistry);
    }
}
