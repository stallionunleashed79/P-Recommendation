package com.cat.digital.reco.stepDefinitions;

import java.io.IOException;

import com.cat.digital.reco.entitlements.response.UserEntitlementsResponse;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import org.junit.Assert;

public class Helper {

    @Given("I am a {string} in Foresight")
    public void userAuthorization (String userType) throws IOException {
        switch (userType) {
            case "CMA":
                var json = "{\"authorization\":{\"catrecid\":\"QPS-31E9B6DF\",\"permissions\":{\"recommendations\":{\"view\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"CAT\",\"B030\"],\"regions\":[\"ADSD-N\"]}]},\"create\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"T030\"]}]},\"update\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"CAT\",\"B030\"],\"regions\":[\"ADSD-N\"]}]},\"delete\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"B030\"]}]}}}}}";
                var userEntitlementsResponse = getUserEntitlementsResponse(json);
                Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getView());
                Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getCreate());
                Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getUpdate());
                Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getDelete());
                break;
            case "user":
                json = "{\"authorization\": {\"catrecid\": \"QPS-31E9B6DF\",\"permissions\": {\"recommendations\": {\"view\": {\"allowedFields\": [\"*\"],\"filterConditions\": [{\"partyNumbers\": [\"T030\"]}]}}}}}";
                userEntitlementsResponse = getUserEntitlementsResponse(json);
                Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getView());
                Assert.assertNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getCreate());
                Assert.assertNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getUpdate());
                Assert.assertNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getDelete());
                break;
        }
    }

    @Given("I am a dealer user in Foresight")
    public void dealerUser () throws IOException {
        var json = "{\"authorization\":{\"catrecid\":\"QPS-31E9B6DF\",\"permissions\":{\"recommendations\":{\"view\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"T030\"]}]},\"create\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"T030\"]}]},\"update\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"CAT\",\"B030\"],\"regions\":[\"ADSD-N\"]}]},\"delete\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"B030\"]}]}}}}}";
        var userEntitlementsResponse = getUserEntitlementsResponse(json);
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getView());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getCreate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getUpdate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getDelete());
    }

    @Given("I am an unauthorized dealer user in Foresight")
    public void unAuthorizedDealerUser () throws IOException {
        var json = "{\"authorization\":{\"catrecid\":\"QPS-31E9B6DF\",\"permissions\":{\"recommendations\":{\"view\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"B030\"]}]},\"create\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"T030\"]}]},\"update\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"CAT\",\"B030\"],\"regions\":[\"ADSD-N\"]}]},\"delete\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"B030\"]}]}}}}}";
        var userEntitlementsResponse = getUserEntitlementsResponse(json);
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getView());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getCreate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getUpdate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getDelete());
    }

    @Given("I am a user without view role for the recommendations domain")
    public void unauthorizedUser () throws IOException {
        var jsonWithoutViewRole = "{\"authorization\":{\"catrecid\":\"QPS-0001B48E\",\"permissions\":{\"recommendations\":{\"create\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"T030\"]}]},\"update\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"CAT\",\"B030\"],\"regions\":[\"ADSD-N\"]}]},\"delete\":{\"allowedFields\":[\"*\"],\"filterConditions\":[{\"partyNumbers\":[\"B030\"]}]}}}}}";
        var userEntitlementsResponse = getUserEntitlementsResponse(jsonWithoutViewRole);
        Assert.assertNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getView());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getCreate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getUpdate());
        Assert.assertNotNull(userEntitlementsResponse.getAuthorization().getPermissions().getRecommendations().getDelete());
    }

    /**
     * This method will be removed once we have the UserEntitlementsResponse
     * */
    private UserEntitlementsResponse getUserEntitlementsResponse(final String json) throws IOException {
        return IntegrationTestMapper.fromJson(json, new TypeReference<>() {
        });
    }
}