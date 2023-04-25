package com.cat.digital.reco.stepDefinitions;

import java.util.*;

import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.enums.UserType;
import com.cat.digital.reco.generateRandomRecommendation.GenerateRandomRecommendation;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.cat.digital.reco.utils.rest.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;

public class CreateRecoImpl {

    public static RecommendationPostRequest recommendationPostRequest;
    public static RecommendationDetailsResponse recommendationPostResponse;

    @And("I have a valid request to create a recommendation")
    public void createRecommendation(RecommendationPostRequest recommendationPostRequest){
        if(recommendationPostRequest!= null){
            CreateRecoImpl.recommendationPostRequest= recommendationPostRequest;
        }
    }

    @And("I have an existing request to create a recommendation")
    public void createRecommendation() throws JsonProcessingException {
        GenerateRandomRecommendation generateRandomRecommendation = new GenerateRandomRecommendation();
        CreateRecoImpl.recommendationPostRequest = generateRandomRecommendation.createRecommendationRequestRandom();
    }

    @When("I make a POST call to the recommendation manager's recommendations resource for {string}")
    public void postRecommendations(String userType) throws JsonProcessingException {
        Request.Builder requestBuilder = new Request.Builder()
                .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationPostRequest)));
        switch (UserType.valueOf(userType)){
            case CATALL:
                IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/", EntitlementValue.CATALL, null);
                break;
            case CAT:
                IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/", EntitlementValue.CAT, null);
                break;
            case dealer:
                IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/", EntitlementValue.DEALER, null);
                break;
        }
    }

    @When("I make a POST call to the recommendation manager's recommendations resource for unauthorized dealer")
    public void postRecommendationsForUnAuthDealer() throws JsonProcessingException {
        Request.Builder requestBuilder = new Request.Builder()
                .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationPostRequest)));
        IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/", EntitlementValue.UNAUTHDEALERCODE, null);
    }

    @And("I make a POST call to the recommendation manager's recommendations resource with a request missing {string}")
    public void createRecommendationMissingReqField(final String requiredField, final RecommendationPostRequest recommendationPostRequest) throws JsonProcessingException {
        if (Objects.isNull(recommendationPostRequest) || StringUtils.isBlank(requiredField)) {
            return;
        }
        var recommendationJsonObject = IntegrationTestMapper.removeRequiredFieldFromRequest(
            requiredField, recommendationPostRequest);
        Request.Builder requestBuilder = new Request.Builder()
                .post(RequestBody.create(RestClient.JSON, recommendationJsonObject.toString()));
        IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/", EntitlementValue.CATALL, null);
    }
}