package com.cat.digital.reco.generateRandomRecommendation;

import com.cat.digital.reco.domain.models.*;
import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandomRecommendation {
    private Random random = new Random();

    public String selectRandomAssetId(){
        List<String> assetIdList = Arrays.asList("CAT|RJG04184|2969484566",
                "CAT|ERM00237|2969484566",
                "CAT|SSP00472|2969484566",
                "CAT|ERM00235|2969484566");
        return assetIdList.get(random.nextInt(assetIdList.size()));
    }

    public String selectRandomTemplateName(){
        List<String> templateNameList = Arrays.asList("Template1Default");
        return templateNameList.get(random.nextInt(templateNameList.size()));
    }

    public BaseOwner selectRandomCatrecId(){
        List<String> catrecIdList = Arrays.asList("QPS-0001B48E");
        String catrecIdRandom = catrecIdList.get(random.nextInt(catrecIdList.size()));
        BaseOwner baseOwner = new BaseOwner();
        baseOwner.setCatrecid(catrecIdRandom);
        return baseOwner;
    }

    public BigDecimal selectRandomReading(Double min, Double max){
        double value = min + random.nextDouble() * (max - min);
        return new BigDecimal(value);
    }

    public String selectRandomUnitofMeasure(){
        List<String> unitofMeasureList = Arrays.asList("h","hours");
        return unitofMeasureList.get(random.nextInt(unitofMeasureList.size()));
    }

    public HoursReading selectRandomHoursReading(){
        BigDecimal reading = this.selectRandomReading(10D, 5000D).setScale(2, RoundingMode.HALF_UP);
        String unitOfMeasure = this.selectRandomUnitofMeasure();
        HoursReading hoursReading = new HoursReading();
        hoursReading.setReading(reading);
        hoursReading.setUnitOfMeasure(unitOfMeasure);
        return hoursReading;
    }

    public String selectRandomSite(){
        List<String> siteList = Arrays.asList("Asset location","Glencore Rolleston","Ravensworth","Glencore Newlands"
                ,"Caterpillar Proving Grounds"," ");
        return siteList.get(random.nextInt(siteList.size()));
    }

    public String selectRandomTitle(){
        int lowerLimit = 97;
        int upperLimit = 122;
        int lenght = 5;
        StringBuilder stringBuilder = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
            int nextRandomChar = lowerLimit + (int)
                    (random.nextFloat() * (upperLimit - lowerLimit + 1));
            stringBuilder.append((char)nextRandomChar);
        }
        return ("Draft Recommendation" + " - " + stringBuilder.toString());
    }

    public OffsetDateTime selectRandomExpirationTime(int min, int max) {
        OffsetDateTime offsetdatetime = OffsetDateTime.now(ZoneId.systemDefault());
        int randomNumber = random.nextInt(max - min) + min;
        offsetdatetime = offsetdatetime.plusDays(randomNumber);
        return offsetdatetime;
    }

    public List<TemplateCustomField> selectRandomPropertyNamePropertyValue
            (List<TemplateCustomField> templateCustomFieldsArrayList){

        TemplateCustomField templateCustomFieldsRandom = templateCustomFieldsArrayList.get(random
                .nextInt(templateCustomFieldsArrayList.size()));

        templateCustomFieldsArrayList = new ArrayList<>();
        templateCustomFieldsArrayList.add(templateCustomFieldsRandom);

        return templateCustomFieldsArrayList;
    }

    public  List<TemplateCustomField> generateTemplateCustomFieldList(){
        List<TemplateCustomField> templateCustomFieldsArrayList = List.of(
                new TemplateCustomField("recommendationPriority", "1 - Immediate Attention"),
                new TemplateCustomField("recommendationPriority", "2 - At Next Stop"),
                new TemplateCustomField("recommendationPriority", "3 - At Next Service"),
                new TemplateCustomField("recommendationPriority", "4 - Equipment Backlog"),
                new TemplateCustomField("recommendationPriority", "5 - Information/Monitor")
        );
        return templateCustomFieldsArrayList;
    }
    public  List<TemplateCustomField> generateTemplateCustomFieldStatusList(){
        List<TemplateCustomField> templateCustomFieldsArrayList = List.of(
                new TemplateCustomField("recommendationStatus", "Draft"),
                new TemplateCustomField("recommendationStatus", "Published"),
                new TemplateCustomField("recommendationStatus", "Completed"),
                new TemplateCustomField("recommendationStatus", "Expired")
                );
        return templateCustomFieldsArrayList;
    }

    public  List<TemplateCustomField> generateTemplateCustomFieldUpdateReco(){
        List<TemplateCustomField> templateCustomFieldsArrayList = this.selectRandomPropertyNamePropertyValue
                (this.generateTemplateCustomFieldList());
        templateCustomFieldsArrayList.addAll(this.selectRandomPropertyNamePropertyValue
                (this.generateTemplateCustomFieldStatusList()));
        return templateCustomFieldsArrayList;
    }

    public RecommendationPostRequest createRecommendationRequestRandom(){
        RecommendationPostRequest recommendationPostRequest = new RecommendationPostRequest();
        recommendationPostRequest.setAssetId(this.selectRandomAssetId());
        recommendationPostRequest.setTemplateName(this.selectRandomTemplateName());
        recommendationPostRequest.setOwner(this.selectRandomCatrecId());
        recommendationPostRequest.setHoursReading(this.selectRandomHoursReading());
        recommendationPostRequest.setSite(this.selectRandomSite());
        recommendationPostRequest.setTitle(this.selectRandomTitle());
        recommendationPostRequest.setExpirationTime(this.selectRandomExpirationTime(1,364));
        recommendationPostRequest.setTemplateCustomProperties(this.selectRandomPropertyNamePropertyValue
                (this.generateTemplateCustomFieldList()));
        return recommendationPostRequest;
    }

    public RecommendationPutRequest updateRecommendationRequestRandom(){
        RecommendationPutRequest recommendationPutRequest = new RecommendationPutRequest();
        recommendationPutRequest.setOwner(this.selectRandomCatrecId());
        recommendationPutRequest.setHoursReading(this.selectRandomHoursReading());
        recommendationPutRequest.setSite(this.selectRandomSite());
        recommendationPutRequest.setTitle(this.selectRandomTitle());
        recommendationPutRequest.setExpirationTime(this.selectRandomExpirationTime(1,364));
        recommendationPutRequest.setTemplateCustomProperties(this.generateTemplateCustomFieldUpdateReco());
        return recommendationPutRequest;
    }
}
