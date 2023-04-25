@301558
Feature: As a foresight user, the recommendation summary list should only include recommendations that I am entitled to
  Background:
    Given I am a dealer user in Foresight
    And I have a valid request to create a recommendation
      | assetId                     | templateName             |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue       |
      | CAT\|ERM00237\|2969484566   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service |
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    Then I expect the endpoint to return an http 201 response code and added to included list
    And I am an unauthorized dealer user in Foresight
    And I have a valid request to create a recommendation
      | assetId                     | templateName             |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue       |
      | CAT\|ERM00239\|2969484569   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service |
    When I make a POST call to the recommendation manager's recommendations resource for unauthorized dealer
    Then I expect the endpoint to return an http 201 response code and added to excluded list

  @smoke
  Scenario: Dealer user can retrieve recommendations in Foresight
    Given I am a dealer user in Foresight
    When I as "dealer" user make a POST request to the recommendation service's \recommendations\search endpoint
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 200 response code
    And I expect the endpoint to return object "recommendations" with only one recommendations in include list
