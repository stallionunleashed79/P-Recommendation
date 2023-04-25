package com.cat.digital.reco.stepDefinitions;

import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cat.digital.reco.common.Constants.NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_ROLE_MSG;
import static com.cat.digital.reco.common.Constants.NOT_HEADER_FORBIDDEN_MSG;
import static org.junit.Assert.*;


public class ValidationImpl {

    private final ObjectMapper mapper = new ObjectMapper();

    @When("I expect the endpoint to return an http {int} response code")
    public void okStatusResponse(int expectedStatus) {
        assertEquals(expectedStatus, IntegrationTestMapper.response.status);
    }

    @When("I expect the endpoint to return an http {int} response code and added to included list")
    public void okStatusResponseAndAddIncludeList(int expectedStatus) throws JsonProcessingException {
        assertEquals(expectedStatus, IntegrationTestMapper.response.status);
        String recommendationNumber = mapper.readValue(IntegrationTestMapper.response.body, Map.class).get("recommendationNumber").toString();
        SearchRecoImpl.recommendationsIncluded.put(recommendationNumber, false);
    }

    @When("I expect the endpoint to return an http {int} response code and added to excluded list")
    public void okStatusResponseAndAddExcludeList(int expectedStatus) throws JsonProcessingException {
        assertEquals(expectedStatus, IntegrationTestMapper.response.status);
        String recommendationNumber = mapper.readValue(IntegrationTestMapper.response.body, Map.class).get("recommendationNumber").toString();
        SearchRecoImpl.recommendationsNotIncluded.put(recommendationNumber, false);
    }

    @And("I expect the error message to be {string}")
    public void validateErrorMessage(String errorMessage) {
        assertTrue(IntegrationTestMapper.response.body.contains(errorMessage));
    }

    @Then("I expect the response body to contain a {string} field")
    public void validateResponseContainsField(String field) {
        Assert.assertTrue(IntegrationTestMapper.response.body.contains(field));
    }

    @Then("I expect the response body to not contain a {string} field")
    public void validateResponseNotContainsField(String field) {
        Assert.assertFalse(IntegrationTestMapper.response.body.contains(field));
    }

    @Then("I expect the {string} property in the response body to contain {string}")
    public void validateResponseContainsValue(final String key, final String value) {
        var documentContext = JsonPath.parse(IntegrationTestMapper.response.body);
        var path = "$..".concat(key);
        var section = documentContext.read(path).toString();
        Assert.assertTrue(section.contains(value));
    }

    @Then("I expect this {string} object to contain {string}")
    public void validateResponseChildHeaderField(String parent, String child) throws JSONException {
        if(parent.equals("commonFields")){
            Assert.assertFalse(new JSONObject(IntegrationTestMapper.response.body).getJSONObject(parent).isNull(child));
        }else if(parent.equals("productFamily")){
            Assert.assertFalse(new JSONObject(IntegrationTestMapper.response.body).getJSONObject("commonFields").getJSONObject("assetMetadata").getJSONObject(parent).isNull(child));
        } else{
            Assert.assertFalse(new JSONObject(IntegrationTestMapper.response.body).getJSONObject("commonFields").getJSONObject(parent).isNull(child));
        }
    }

    @Then("I expect each object in {string} array to contain {string}")
    public void validateResponseContainsChildListField(String parent, String child) throws JSONException {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));
        int counter = 0;
        for(int i=0; i<jsonArray.length(); i++)  {
            if(!jsonArray.getJSONObject(i).isNull(child)){
                counter++;
            }
        }
        Assert.assertEquals(jsonArray.length(),counter);
    }
    @Then("I expect the endpoint to return a {string} response message")
    public void validateResponseContainsMessage(String message) {
        Assert.assertTrue(IntegrationTestMapper.response.body.contains(message));
    }

    @When("I expect the GET endpoint to return an invalid entitlement header response message")
    public void forbiddenUserStatusResponse() {
        assertTrue(IntegrationTestMapper.response.body.contains(
            NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_ROLE_MSG));
    }

    @When("I expect the GET endpoint to return an {int} error code")
    public void unauthorizedDealerResponse(final int expectedStatus) {
        assertTrue(IntegrationTestMapper.response.body.contains(
            NOT_HEADER_FORBIDDEN_MSG));
        assertEquals(IntegrationTestMapper.response.status, expectedStatus);
    }

    @And("I expect the endpoint to return object {string} with recommendations array")
    public void validateSearchResponseArray(String parent) {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));

        Assertions.assertThat(jsonArray.length()).isGreaterThan(1);
    }

    @And("I expect {string} object to contain {string} field")
    public void validateSearchResponseField(String parent, String field) throws JsonProcessingException {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));
        final Map resultMap = mapper.readValue(jsonArray.get(jsonArray.length()-1).toString(), Map.class);
        Assertions.assertThat(resultMap.get(field)).isNotNull();
    }

    @And("I expect each recommendation within {string} to contain an {string} object object with {string} of the recommendation owner")
    public void validateSearchResponseFieldOwnerObject(String parent, String subParent, String field) throws JsonProcessingException {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));
        for(int i=0; i<jsonArray.length()-1;i++) {
            String subParentResult = mapper.readTree(jsonArray.get(i).toString()).get(subParent).toString();
            final Map resultMap = mapper.readValue(subParentResult, Map.class);
            Assertions.assertThat(resultMap.get(field)).isNotNull();
        }
    }

    @And("I expect the endpoint to return object {string} with both recommendations in include list")
    public void validateSearchBothRecommendations(String parent) throws JsonProcessingException {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));
        Map<String, Boolean> recommendationsIncluded = SearchRecoImpl.recommendationsIncluded;
        for(int i=0; i<jsonArray.length();i++) {
            JsonNode recommendationNumber = mapper.readTree(jsonArray.get(i).toString()).get("recommendationNumber");
            if(recommendationsIncluded.containsKey(recommendationNumber.asText())) {
                recommendationsIncluded.put(recommendationNumber.asText(), true);
            }
        }
        boolean isNotIncluded = recommendationsIncluded.containsValue(false);
        Assertions.assertThat(recommendationsIncluded.size()).isEqualTo(2);
        Assertions.assertThat(isNotIncluded).isEqualTo(false);
        recommendationsIncluded.clear();
    }

    @And("I expect the endpoint to return object {string} with only one recommendations in include list")
    public void validateSearchSingleRecommendations(String parent) throws JsonProcessingException {
        JSONArray jsonArray = (new JSONObject(IntegrationTestMapper.response.body).getJSONArray(parent));
        Map<String, Boolean> recommendationsIncluded = SearchRecoImpl.recommendationsIncluded;
        Map<String, Boolean> recommendationsNotIncluded = SearchRecoImpl.recommendationsNotIncluded;
        for(int i=0; i<jsonArray.length();i++) {
            JsonNode recommendationNumber = mapper.readTree(jsonArray.get(i).toString()).get("recommendationNumber");
            if(recommendationsIncluded.containsKey(recommendationNumber.asText())) {
                recommendationsIncluded.put(recommendationNumber.asText(), true);
            }
            if(recommendationsNotIncluded.containsKey(recommendationNumber.asText())) {
                recommendationsNotIncluded.put(recommendationNumber.asText(), true);
            }
        }
        boolean isNotIncluded = recommendationsIncluded.containsValue(false);
        Assertions.assertThat(recommendationsIncluded.size()).isEqualTo(1);
        Assertions.assertThat(isNotIncluded).isEqualTo(false);
        boolean isIncluded = recommendationsNotIncluded.containsValue(true);
        Assertions.assertThat(recommendationsNotIncluded.size()).isEqualTo(1);
        Assertions.assertThat(isIncluded).isEqualTo(false);
        recommendationsIncluded.clear();
        recommendationsNotIncluded.clear();
    }
}