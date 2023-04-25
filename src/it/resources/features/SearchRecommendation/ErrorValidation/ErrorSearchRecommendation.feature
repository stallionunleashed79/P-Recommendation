@308480
Feature: As a foresight user, I am not authorized to retrieve the summary list
  @smoke
  Scenario: Dealer user is not authorized to retrieve recommendation summary list
    Given I am an unauthorized dealer user in Foresight
    When I as dealer user with no view role make a POST request without view role to the recommendation service's \recommendations\search endpoint
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 403 response code
    And I expect the error message to be "Forbidden, User doesn't have permissions to perform the operation"

  @smoke
  Scenario: CAT user is not authorized to retrieve recommendation summary list
    Given I am a user without view role for the recommendations domain
    When I as CAT user make a POST request without view role to the recommendation service's \recommendations\search endpoint
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 403 response code
    And I expect the error message to be "Forbidden, User doesn't have permissions to perform the operation"
