package com.cat.digital.reco.stepDefinitions;

import java.io.IOException;

import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.enums.UserType;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class GetRecoImpl {
    @When("I am able to retrieve that recommendation")
    public void validateGetRecommendation() throws IOException {
        CreateRecoImpl.recommendationPostResponse = IntegrationTestMapper.fromJson(IntegrationTestMapper.response.body, new TypeReference<>() {
        });
        Assert.assertNotNull(CreateRecoImpl.recommendationPostResponse);
    }

    @When("I make a GET request as a {string} user to the recommendation service's \\recommendation\\ {string} endpoint")
    public void getRecommendation(String userType, String recommendationNumber) {
        switch (UserType.valueOf(userType)){
            case CATALL:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ recommendationNumber, EntitlementValue.CATALL,null);
                break;
            case CAT:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ recommendationNumber, EntitlementValue.CAT,null);
                break;
        }
    }

    @When("I make a GET request without view role to the recommendation service's \\recommendations\\ recommendationNumber endpoint")
    public void getRecommendationWithoutViewRole() {
        IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.DEALERWITHOUTVIEWROLE,null);
    }

    @When("I make a GET request as a {string} user to the recommendation service's \\recommendation\\recommendationNumber endpoint")
    public void getRecommendation(String userType) {
        switch (UserType.valueOf(userType)){
            case CATALL:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.CATALL,null);
                break;
            case CAT:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.CAT,null);
                break;
            case dealer:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.DEALER,null);
                break;
        }
    }

    @When("I make a GET request as an unauthorized {string} user to the recommendation service's \\recommendations\\ recommendationNumber endpoint")
    public void getRecommendationAsUnauthorizedDealer(String userType) {
        switch (UserType.valueOf(userType)){
            case dealer:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.UNAUTHORIZEDDEALER,null);
                break;
            case CAT:
                IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.UNAUTHORIZEDCATUSER,null);
                break;
        }
    }

    @When("I make a GET request as a CAT user without region code to the recommendation service's \\recommendation\\recommendationNumber endpoint")
    public void getRecommendationAsCATUserWithoutRegionCode() {
        IntegrationTestMapper.makeGetRequest("recommendations/v1/recommendations/"+ CreateRecoImpl.recommendationPostResponse.getRecommendationNumber(),EntitlementValue.CATUSERWITHOUTREGIONCODE,null);
    }
}
