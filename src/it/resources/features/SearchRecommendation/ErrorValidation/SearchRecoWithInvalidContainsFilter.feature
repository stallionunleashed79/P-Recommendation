@301558
Feature: Feature: As a CMA in foresight, I should be able to export the recommendation summary list
  Background:
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId                     | templateName             |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue       |
      | CAT\|ERM00237\|2969484566   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code

  @smoke
  Scenario:  Search recommendations with invalid contains filter
    Given I am a "CMA" in Foresight
    And I have a request to search recommendations with an invalid "serialNumber1" contains filter
    When I call the recommendation service POST \recommendations\search endpoint with accept header as "application/json"
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 400 response code
    And I expect the error message to be "serialNumber1 is not a valid contains filter."
