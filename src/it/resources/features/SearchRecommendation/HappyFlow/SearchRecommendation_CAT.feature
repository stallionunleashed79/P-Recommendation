@301558
Feature: As a foresight user, the recommendation summary list should only include recommendations that I am entitled to
  Background:
    Given I am a dealer user in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    Then I expect the endpoint to return an http 201 response code and added to included list
    And I am an unauthorized dealer user in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code and added to included list

  @smoke
  Scenario: Cat User can retrieve all recommendations
    Given I am a "CMA" in Foresight
    When I as "CAT" user make a POST request to the recommendation service's \recommendations\search endpoint
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC, DESC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 200 response code
    And I expect the endpoint to return object "recommendations" with both recommendations in include list
