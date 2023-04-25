@307111
Feature: As a CMA in Foresight, I need to retrieve the recommendation template properties

  Background:
    Given I am a "user" in Foresight
    When I send a GET request to the recommendation manager's \templates\ "Template1Default" endpoint

  @smoke
  Scenario: Successful retrieval of template properties
    Then I expect the endpoint to return an http 200 response code

  @high
  Scenario: Default template contains correct sections
    Then I expect it to include an object with sectionName "Recommendation"
    And I expect the sectionPosition of this section is 0
    And I expect sectionContainerType type is "FRAGMENT_FORM_3"
    Then I expect it to include an object with sectionName "Recommendation Details"
    And I expect the sectionPosition of this section is 1
    And I expect sectionContainerType type is "ACCORDION_FORM_1"
    Then I expect it to include an object with sectionName "Links & Attachments"
    And I expect the sectionPosition of this section is 2
    And I expect sectionContainerType type is "ACCORDION_LINKSATTACHMENTS"
    And I expect this section will have an empty sectionProperties array
    Then I expect it to include an object with sectionName "Value Estimate"
    And I expect the sectionPosition of this section is 3
    And I expect sectionContainerType type is "ACCORDION_FORM_3"
    Then I expect it to include an object with sectionName "Related Exceptions"
    And I expect the sectionPosition of this section is 4
    And I expect sectionContainerType type is "ACCORDION_TABLE_EXCEPTION"
    And I expect this section will have an empty sectionProperties array

  @high
  Scenario: Default Template - Recommendation
    When I look at the "Recommendation" section of the response
    Then I expect this section to contain an entry for "recommendationStatus"
    And I expect this section to contain an entry for "title"
    And I expect this section to contain an entry for "recommendationPriority"
    And I expect this section to contain an entry for "smu"
    And I expect this section to contain an entry for "recommendationNumber"
    And I expect this section to contain an entry for "recommendationOwner"
    And I expect this section to contain an entry for "expirationTime"
    And I expect this section to contain an entry for "assetId"
    And I expect this section to contain an entry for "customer"
    And I expect this section to contain an entry for "site"
    And I expect this section to contain an entry for "recommendationAction"
    And I expect this section to contain an entry for "createdTime"
    And I expect this section to contain an entry for "createdBy"
    And I expect this section to contain an entry for "updatedTime"
    And I expect this section to contain an entry for "updatedBy"

  @high
  Scenario: Default Template - Recommendation Details
    When I look at the "Recommendation Details" section of the response
    Then I expect this section to contain an entry for "recommendationDetails"

  @high
  Scenario: Default Template - Value Estimate
    When I look at the "Value Estimate" section of the response
    Then I expect this section to contain an entry for "valueEstimateCurrency"
    Then I expect this section to contain an entry for "valueEstimateFailureCost"
    Then I expect this section to contain an entry for "valueEstimateRepairCost"
    Then I expect this section to contain an entry for "valueEstimateRecommendationValue"

  @low
  Scenario: Attempt to retrieve a template that does not exist
    Given I am a "user" in Foresight
    When I send a GET request to the recommendation manager's \templates\ "invalid template name" endpoint
    Then I expect the endpoint to return an http 404 response code