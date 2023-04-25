@301558
Feature: Feature: As a CMA in foresight, I should be able to export the recommendation summary list
  Background:
    Given I am a "CMA" in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code

  @smoke
  Scenario:  Search recommendations with invalid stringEquals filter
    Given I am a "CMA" in Foresight
    And I have a request to search recommendations with an invalid "recommendationStatus1" stringEquals filter
    When I call the recommendation service POST \recommendations\search endpoint with accept header as "application/json"
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 400 response code
    And I expect the error message to be "recommendationStatus1 is not a valid stringEquals filter."