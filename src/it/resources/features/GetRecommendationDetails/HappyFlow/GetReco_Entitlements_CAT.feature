@292114
Feature: As a CAT user i want to be able to successfully retrieve existing recommendations in Foresight

  Background:
    Given I have an existing request to create a recommendation

  @smoke
  Scenario Outline: CAT user can retrieve an existing recommendation created by a CAT or dealer user
    Given I am a "CAT USER" in Foresight
    When I make a POST call to the recommendation manager's recommendations resource for <userType>
    And I am able to retrieve that recommendation
    And I make a GET request as a "CAT" user to the recommendation service's \recommendation\recommendationNumber endpoint
    And I expect the response body to contain a "recommendationNumber" field
    Then I expect the endpoint to return an http 200 response code

    Examples:
      |userType   |
      |"dealer"   |
      |"CAT"      |

  @smoke
  Scenario Outline: CAT user without region code retrieves an existing recommendation created by CAT or dealer users
    Given I am a "CAT USER" in Foresight
    When I make a POST call to the recommendation manager's recommendations resource for <userType>
    And I am able to retrieve that recommendation
    And I make a GET request as a CAT user without region code to the recommendation service's \recommendation\recommendationNumber endpoint
    And I expect the response body to contain a "recommendationNumber" field
    Then I expect the endpoint to return an http 200 response code

    Examples:
      |userType   |
      |"dealer"   |
      |"CAT"      |