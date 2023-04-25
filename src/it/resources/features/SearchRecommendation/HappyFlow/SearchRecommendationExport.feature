@301558
Feature: Feature: As a CMA in foresight, I should be able to export the recommendation summary list
  Background:
    Given I am a "CMA" in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code

  @smoke
  Scenario:  Successfully Export Recommendations
    Given I am a "CMA" in Foresight
    And I have a valid request to search recommendations with a "recommendationStatus" stringEquals filter with value "Draft"
    When I call the recommendation service POST \recommendations\search endpoint with accept header as "application/octet-stream"
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 200 response code

