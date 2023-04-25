package com.cat.digital.reco.stepDefinitions;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

public class GetTemplateImpl {
  private static JSONObject selectedSection;

  @When("I send a GET request to the recommendation manager's \\templates\\ {string} endpoint")
  public void getRecommendation(String templateName) {
    IntegrationTestMapper.makeGetRequest("recommendations/v1/templates/"+ templateName, EntitlementValue.CATALL,null);
  }

  @When("I send a GET request to the recommendation manager's \\templates\\ {string} endpoint with role {string}")
  public void getRecommendationWithout(String templateName, String role) {
    IntegrationTestMapper.makeGetRequest("recommendations/v1/templates/"+ templateName, EntitlementValue.valueOf(role),null);
  }

  @Then("I expect it to include an object with sectionName {string}")
  @When("I look at the {string} section of the response")
  public void validateSectionName(String sectionName) {
    var jsonArray = new JSONArray(IntegrationTestMapper.response.body);
    var isSectionContained = false;
    for (int i=0; i<jsonArray.length() && !isSectionContained; i++) {
      selectedSection = jsonArray.getJSONObject(i);
      isSectionContained = selectedSection.getString("sectionName").equals(sectionName);
    }
    Assert.assertTrue(isSectionContained);
  }

  @And("I expect the sectionPosition of this section is {long}")
  public void validateSectionPosition(long sectionPosition) {
    Assert.assertEquals(sectionPosition, selectedSection.getInt("sectionPosition"));
  }

  @And("I expect sectionContainerType type is {string}")
  public void validateSectionContainer(String containerType) {
    Assert.assertEquals(selectedSection.getJSONObject("sectionContainerType").getString("type"), containerType);
  }

  @And("I expect this section will have an empty sectionProperties array")
  public void validateSectionProperties() {
    Assert.assertEquals(0, selectedSection.getJSONArray("sectionProperties").length());
  }

  @Then("I expect this section to contain an entry for {string}")
  public void validateSectionProperty(String sectionPropertyName) {
    var propertyArray = selectedSection.getJSONArray("sectionProperties");
    var isPropertyContained = false;
    for (int i = 0; i < propertyArray.length() && !isPropertyContained; i ++) {
      isPropertyContained = propertyArray.getJSONObject(i).getString("propertyName").equals(sectionPropertyName);
    }

    Assert.assertTrue(isPropertyContained);
  }
}
