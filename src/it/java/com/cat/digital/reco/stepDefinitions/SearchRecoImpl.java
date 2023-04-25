package com.cat.digital.reco.stepDefinitions;

import com.cat.digital.reco.domain.models.ResponseAttribute;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.filter.type.ContainsFilter;
import com.cat.digital.reco.filter.type.StringEqualsFilter;
import com.cat.digital.reco.model.RecommendationSearchQueryParam;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.cat.digital.reco.utils.rest.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.*;

import static com.cat.digital.reco.common.Constants.*;
import static org.springframework.http.MediaType.*;

public class SearchRecoImpl {

  public static RecommendationSearchQueryParam recommendationSearchQueryParam;

  public static RecommendationSearchRequest recommendationSearchRequest = new RecommendationSearchRequest();

  public static Map<String, Boolean> recommendationsIncluded = new HashMap<>();
  public static Map<String, Boolean> recommendationsNotIncluded = new HashMap<>();

  @And("I have a valid request to search recommendations with a {string} stringEquals filter with value {string}")
  public void searchRequestRecommendationWithStringEqualsFilter(final String propertyName, final String propertyValue){
    var stringEqualsFilter = new StringEqualsFilter();
    stringEqualsFilter.setPropertyName("recommendationStatus");
    stringEqualsFilter.setValues(List.of("Draft"));

    recommendationSearchRequest.setFilters(Collections.singletonList(stringEqualsFilter));
    recommendationSearchRequest.setLogicalExpression("$0");
    recommendationSearchRequest.setResponseAttributes(Collections.singletonList(ResponseAttribute.serialNumber));
  }

  @And("I have a valid request to search recommendations with a {string} contains filter with value {string}")
  public void searchRequestRecommendationWithMultipleFilters(final String propertyName, final String propertyValue){
    var containsFilter = new ContainsFilter();
    containsFilter.setPropertyName(propertyName);
    containsFilter.setValue(propertyValue);

    recommendationSearchRequest.setFilters(Collections.singletonList(containsFilter));
    recommendationSearchRequest.setLogicalExpression("$0");
    recommendationSearchRequest.setResponseAttributes(Collections.singletonList(ResponseAttribute.serialNumber));
  }

  @And("I have a valid request to search recommendations with multiple filters with entries {string}, {string}, {string}, {string}")
  public void searchRequestRecommendationWithContainsFilter(final String firstPropertyName, final String firstPropertyValue,
                                                            final String secondPropertyName, final String secondPropertyValue){
    var containsFilter = new ContainsFilter();
    containsFilter.setPropertyName(firstPropertyName);
    containsFilter.setValue(firstPropertyValue);

    var stringEqualsFilter = new StringEqualsFilter();
    stringEqualsFilter.setPropertyName(secondPropertyName);
    stringEqualsFilter.setValues(List.of(secondPropertyValue));

    recommendationSearchRequest.setFilters(Arrays.asList(stringEqualsFilter, containsFilter));
    recommendationSearchRequest.setLogicalExpression("$0");
    recommendationSearchRequest.setResponseAttributes(Collections.singletonList(ResponseAttribute.serialNumber));
  }

  @And("I have a request to search recommendations with an invalid {string} stringEquals filter")
  public void searchRecommendationsWithInvalidStringEqualsFilter(final String invalidPropertyName){
    var containsFilter = new StringEqualsFilter();
    containsFilter.setPropertyName(invalidPropertyName);
    containsFilter.setValues(Collections.singletonList("Draft"));

    recommendationSearchRequest.setFilters(Collections.singletonList(containsFilter));
    recommendationSearchRequest.setLogicalExpression("$0");
    recommendationSearchRequest.setResponseAttributes(Collections.singletonList(ResponseAttribute.serialNumber));
  }

  @And("I have a request to search recommendations with an invalid {string} contains filter")
  public void searchRecommendationsWithInvalidContainsFilter(final String invalidPropertyName){
    var containsFilter = new ContainsFilter();
    containsFilter.setPropertyName(invalidPropertyName);
    containsFilter.setValue("RJG");

    recommendationSearchRequest.setFilters(Collections.singletonList(containsFilter));
    recommendationSearchRequest.setLogicalExpression("$0");
    recommendationSearchRequest.setResponseAttributes(Collections.singletonList(ResponseAttribute.serialNumber));
  }

  @When("I call the recommendation service POST \\recommendations\\search endpoint")
  public void searchRecommendationsForCATWithoutFilters(RecommendationSearchQueryParam recommendationSearchQueryParam) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
        .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationSearchRequest)));
    HashMap<Object, Object> queryParam = new HashMap<>() {{
      put(SORT_BY, recommendationSearchQueryParam.getSortBy());
      put(ORDER_BY, recommendationSearchQueryParam.getOrderBy());
      put(CURSOR, recommendationSearchQueryParam.getCursor());
      put(LIMIT, recommendationSearchQueryParam.getLimit());
      put(SKIP, recommendationSearchQueryParam.getSkip());
      put(SEARCH_VALUE, recommendationSearchQueryParam.getSearchValue());
    }};
    IntegrationTestMapper.makeRequestQueryParam(requestBuilder, "recommendations/v1/recommendations/search", EntitlementValue.CATALL, new HttpUrl.Builder(), queryParam, APPLICATION_JSON_VALUE);
  }

  @When("I call the recommendation service POST \\recommendations\\search endpoint with accept header as {string}")
  public void postRecommendationsAcceptHeader(String acceptHeaderType, RecommendationSearchQueryParam recommendationSearchQueryParam) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
            .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationSearchRequest)));
    HashMap<Object, Object> queryParam = new HashMap<>() {{
      put(SORT_BY, recommendationSearchQueryParam.getSortBy());
      put(ORDER_BY, recommendationSearchQueryParam.getOrderBy());
      put(CURSOR, recommendationSearchQueryParam.getCursor());
      put(LIMIT, recommendationSearchQueryParam.getLimit());
      put(SKIP, recommendationSearchQueryParam.getSkip());
      put(SEARCH_VALUE, recommendationSearchQueryParam.getSearchValue());
    }};
    IntegrationTestMapper.makeRequestQueryParam(requestBuilder, "recommendations/v1/recommendations/search", EntitlementValue.CATALL, new HttpUrl.Builder(), queryParam, acceptHeaderType);
  }

  @When("I as dealer user with no view role make a POST request without view role to the recommendation service's \\recommendations\\search endpoint")
  public void postRecommendationsForUnauthorizedDealer(RecommendationSearchQueryParam recommendationSearchQueryParam) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
            .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationSearchRequest)));
    HashMap<Object, Object> queryParam = new HashMap<>() {{
      put(SORT_BY, recommendationSearchQueryParam.getSortBy());
      put(ORDER_BY, recommendationSearchQueryParam.getOrderBy());
      put(CURSOR, recommendationSearchQueryParam.getCursor());
      put(LIMIT, recommendationSearchQueryParam.getLimit());
      put(SKIP, recommendationSearchQueryParam.getSkip());
      put(SEARCH_VALUE, recommendationSearchQueryParam.getSearchValue());
    }};
    IntegrationTestMapper.makeRequestQueryParam(requestBuilder, "recommendations/v1/recommendations/search", EntitlementValue.DEALERWITHOUTVIEWROLE, new HttpUrl.Builder(), queryParam, APPLICATION_JSON_VALUE);
  }

  @When("I as CAT user make a POST request without view role to the recommendation service's \\recommendations\\search endpoint")
  public void postRecommendationsForUnauthorizedUser(RecommendationSearchQueryParam recommendationSearchQueryParam) throws JsonProcessingException {
    Request.Builder requestBuilder = new Request.Builder()
            .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationSearchRequest)));
    HashMap<Object, Object> queryParam = new HashMap<>() {{
      put(SORT_BY, recommendationSearchQueryParam.getSortBy());
      put(ORDER_BY, recommendationSearchQueryParam.getOrderBy());
      put(CURSOR, recommendationSearchQueryParam.getCursor());
      put(LIMIT, recommendationSearchQueryParam.getLimit());
      put(SKIP, recommendationSearchQueryParam.getSkip());
      put(SEARCH_VALUE, recommendationSearchQueryParam.getSearchValue());
    }};
    IntegrationTestMapper.makeRequestQueryParam(requestBuilder, "recommendations/v1/recommendations/search", EntitlementValue.CATUSERWITHOUTVIEWROLE, new HttpUrl.Builder(), queryParam, APPLICATION_JSON_VALUE);
  }

  @When("I as {string} user make a POST request to the recommendation service's \\recommendations\\search endpoint")
  public void postRecommendationsForDealer(String userType, RecommendationSearchQueryParam recommendationSearchQueryParam) throws JsonProcessingException {
    EntitlementValue entitlementValue ;
    switch (userType) {
      case "dealer":
        entitlementValue = EntitlementValue.DEALER;
        break;
      case "CATALL":
        entitlementValue = EntitlementValue.CATALL;
        break;
      default:
        entitlementValue = EntitlementValue.CAT;
        break;
    }
    Request.Builder requestBuilder = new Request.Builder()
            .post(RequestBody.create(RestClient.JSON, IntegrationTestMapper.toJson(recommendationSearchRequest)));
    HashMap<Object, Object> queryParam = new HashMap<>() {{
      put(SORT_BY, recommendationSearchQueryParam.getSortBy());
      put(ORDER_BY, recommendationSearchQueryParam.getOrderBy());
      put(CURSOR, recommendationSearchQueryParam.getCursor());
      put(LIMIT, recommendationSearchQueryParam.getLimit());
      put(SKIP, recommendationSearchQueryParam.getSkip());
      put(SEARCH_VALUE, recommendationSearchQueryParam.getSearchValue());
    }};
    IntegrationTestMapper.makeRequestQueryParam(requestBuilder, "recommendations/v1/recommendations/search", entitlementValue, new HttpUrl.Builder(), queryParam, APPLICATION_JSON_VALUE);
  }
}
