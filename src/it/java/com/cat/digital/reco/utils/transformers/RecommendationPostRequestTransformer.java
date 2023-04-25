package com.cat.digital.reco.utils.transformers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;

import com.cat.digital.reco.domain.models.BaseOwner;
import com.cat.digital.reco.domain.models.HoursReading;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.generateRandomRecommendation.GenerateRandomRecommendation;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.datatable.DataTableType;
import lombok.Builder;

@Builder
public class RecommendationPostRequestTransformer {
    public void transform(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(RecommendationPostRequest.class,
                (Map<String, String> row) -> {
                    RecommendationPostRequest recommendationPostRequest = new RecommendationPostRequest();
                    GenerateRandomRecommendation generateRandomRecommendation = new GenerateRandomRecommendation();

                    //Create assetId
                    recommendationPostRequest.setAssetId(!Objects.isNull(row.get("assetId")) ? row.get("assetId"):
                            generateRandomRecommendation.selectRandomAssetId() );

                    //Create templateName
                    recommendationPostRequest.setTemplateName(!Objects.isNull(row.get("templateName")) ?
                            row.get("templateName"): generateRandomRecommendation.selectRandomTemplateName());

                    //Create baseOwner
                    recommendationPostRequest.setOwner(!Objects.isNull(row.get("catrecId")) ?
                            IntegrationTestMapper.getBaseOwnerFromTableRow(row):
                            generateRandomRecommendation.selectRandomCatrecId());

                    //Create hoursReading
                    recommendationPostRequest.setHoursReading(this.transformHoursReading(row, generateRandomRecommendation));

                    //Create site
                    recommendationPostRequest.setSite(!Objects.isNull(row.get("site")) ? row.get("site"):
                            generateRandomRecommendation.selectRandomSite());

                    //Create title
                    recommendationPostRequest.setTitle(!Objects.isNull(row.get("title")) ? row.get("title"):
                            generateRandomRecommendation.selectRandomTitle());

                    //Create
                    recommendationPostRequest.setExpirationTime(!Objects.isNull(row.get("expirationTime"))?
                            OffsetDateTime.parse(row.get("expirationTime")):generateRandomRecommendation
                            .selectRandomExpirationTime(1, 364));

                    //Create templateFields
                    recommendationPostRequest.setTemplateCustomProperties(this.transformTemplateCustomProperties(row,
                            generateRandomRecommendation));

                    return recommendationPostRequest;
                }
        ));
    }

    /**
     * Check value for HoursReading
     * If reading or UnitOfMeasure are null assign random value
     */
    public HoursReading transformHoursReading(Map<String, String> row,
                                      GenerateRandomRecommendation generateRandomRecommendation) {
        if (!Objects.isNull(row.get("reading"))  && !Objects.isNull(row.get("unitOfMeasure"))) {
            return IntegrationTestMapper.getHoursReadingFromTableRow(row);
        } else if (!Objects.isNull(row.get("reading"))) {
            HoursReading hoursReading = new HoursReading();
            hoursReading.setUnitOfMeasure(generateRandomRecommendation.selectRandomUnitofMeasure());
            hoursReading.setReading(new BigDecimal(row.get("reading")));
            return hoursReading;
        } else if (!Objects.isNull(row.get("unitOfMeasure"))) {
            HoursReading hoursReading = new HoursReading();
            hoursReading.setReading(generateRandomRecommendation.selectRandomReading(10D, 5000D)
                    .setScale(2, RoundingMode.HALF_UP));
            hoursReading.setUnitOfMeasure(row.get("unitOfMeasure"));
            return hoursReading;
        } else {
            return generateRandomRecommendation
                    .selectRandomHoursReading();
        }
    }

    /**
     * Check value for templateCustomeField
     * If propertyName or PropertyValue are null assign random value
     */
    public List<TemplateCustomField> transformTemplateCustomProperties(Map<String,
            String> row,
             GenerateRandomRecommendation generateRandomRecommendation) {
        if (!Objects.isNull(row.get("propertyName")) && !Objects.isNull(row.get("propertyValue"))) {
            return IntegrationTestMapper.
                    getTemplateCustomFieldsFromTableRow(row);
        } else if (!Objects.isNull(row.get("propertyName"))) {
            List<TemplateCustomField> filteredList = new ArrayList<>();
            for (TemplateCustomField templateCustomField :
                    generateRandomRecommendation.generateTemplateCustomFieldList()) {
                if (row.get("propertyName").equals(templateCustomField.getPropertyName())) {
                    filteredList.add(templateCustomField);
                }
            }
            return generateRandomRecommendation
                    .selectRandomPropertyNamePropertyValue(filteredList);
        } else if (!Objects.isNull(row.get("propertyValue"))) {
            List<TemplateCustomField> filteredList = generateRandomRecommendation.generateTemplateCustomFieldList();
            TemplateCustomField templateCustomField = filteredList.stream().filter(item ->
                    row.get("propertyValue").equals(item.getPropertyValue())
            ).findFirst().orElse(null);
            return Collections.singletonList(templateCustomField);
        } else {
            return generateRandomRecommendation
                    .selectRandomPropertyNamePropertyValue(generateRandomRecommendation
                            .generateTemplateCustomFieldList());
        }
    }
}

