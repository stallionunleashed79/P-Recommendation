package com.cat.digital.reco.stepDefinitions;

import java.util.Objects;

import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
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

public class UpdateRecoImpl {

  public static final String TEMPLATE_CUSTOM_PROPERTIES = "templateCustomProperties";
  public static RecommendationPutRequest recommendationPutRequest;

  @And("I have a valid request to update a recommendation")
  public static void updateRecommendation(RecommendationPutRequest recommendationPutRequest){
    if(!Objects.isNull(recommendationPutRequest)){
      UpdateRecoImpl.recommendationPutRequest= recommendationPutRequest;
    }
  }

  @And("I have an existing request to update a recommendation")
  public void updateRecommendation() throws JsonProcessingException {
    GenerateRandomRecommendation generateRandomRecommendation = new GenerateRandomRecommendation();
    UpdateRecoImpl.recommendationPutRequest = generateRandomRecommendation.updateRecommendationRequestRandom();
  }

  @When("I make a PUT call to the recommendation manager's recommendations resource as {string}")
  public static void putRecommendations(String userType) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
            .put(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(UpdateRecoImpl.recommendationPutRequest)));
    switch (UserType.valueOf(userType)){
      case CATALL:
         IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" + CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(), EntitlementValue.CATALL, null);
        break;
      case CAT:
         IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" + CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(), EntitlementValue.CAT, null);
        break;
      case dealer:
         IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" + CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(), EntitlementValue.DEALER, null);
        break;
    }
  }

  @When("I make a PUT call to the recommendation manager's recommendations resource as an unauthorized {string}")
  public static void putRecommendationUnauthorized(String userType) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
            .put(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(UpdateRecoImpl.recommendationPutRequest)));
    switch (UserType.valueOf(userType)){
      case CAT:
        IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" + CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(), EntitlementValue.UNAUTHORIZEDCATUSER, null);
        break;
      case dealer:
        IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" + CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(), EntitlementValue.UNAUTHORIZEDDEALER, null);
        break;
    }

  }

  @And("I make a PUT call to the recommendation manager's recommendations resource with a request missing {string}")
  public void updateRecommendationMissingReqField(final String requiredField, final RecommendationPutRequest recommendationPutRequest) throws JsonProcessingException {
    if (Objects.isNull(recommendationPutRequest) || StringUtils.isBlank(requiredField)) {
      return;
    }
    var recommendationJsonObject = IntegrationTestMapper.removeRequiredFieldFromRequest(
        requiredField, recommendationPutRequest);
    Request.Builder requestBuilder = new Request.Builder()
        .put(RequestBody.create(RestClient.JSON, recommendationJsonObject.toString()));
    IntegrationTestMapper.makeRequest(requestBuilder, "recommendations/v1/recommendations/" +
        CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),
        EntitlementValue.DEALER, null);
  }
}
