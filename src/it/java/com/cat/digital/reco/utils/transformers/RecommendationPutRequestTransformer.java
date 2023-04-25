package com.cat.digital.reco.utils.transformers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cat.digital.reco.domain.models.BaseOwner;
import com.cat.digital.reco.domain.models.HoursReading;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
import com.cat.digital.reco.generateRandomRecommendation.GenerateRandomRecommendation;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.datatable.DataTableType;
import lombok.Builder;

@Builder
public class RecommendationPutRequestTransformer {
    public void transform(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(RecommendationPutRequest.class, (Map<String, String> row) -> {
                    RecommendationPutRequest recommendationPutRequest = new RecommendationPutRequest();
                    RecommendationPostRequestTransformer recommendationPostRequestTransformer = new RecommendationPostRequestTransformer();
                    GenerateRandomRecommendation generateRandomRecommendation = new GenerateRandomRecommendation();

                    //Create baseOwner
                    recommendationPutRequest.setOwner(!Objects.isNull(row.get("catrecId")) ?
                                    IntegrationTestMapper.getBaseOwnerFromTableRow(row) :
                                    generateRandomRecommendation.selectRandomCatrecId());

                    //Create hoursReading
                    recommendationPutRequest.setHoursReading(recommendationPostRequestTransformer.transformHoursReading
                            (row, generateRandomRecommendation));

                    //Create site
                    recommendationPutRequest.setSite(!Objects.isNull(row.get("site")) ?
                            row.get("site"): generateRandomRecommendation.selectRandomSite() );

                    //Create title
                    recommendationPutRequest.setTitle(!Objects.isNull(row.get("title")) ?
                            row.get("title"): generateRandomRecommendation.selectRandomTitle() );

                    //Create expirationTime
                    recommendationPutRequest.setExpirationTime(!Objects.isNull(row.get("expirationTime"))?
                            OffsetDateTime.parse(row.get("expirationTime")):generateRandomRecommendation
                            .selectRandomExpirationTime(1, 364));

                    //Create templateFields
                    final List<TemplateCustomField> fields = IntegrationTestMapper.
                            getTemplateCustomFieldsFromTableRow(row);

                    recommendationPutRequest.setTemplateCustomProperties(fields.size() == 0 ?
                            generateRandomRecommendation.generateTemplateCustomFieldUpdateReco():
                            fields);

                    return recommendationPutRequest;
                }
                )
        );
    }
}

