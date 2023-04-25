@292114
Feature: As a dealer user i am not authorized to retrieve existing recommendations in Foresight

  Background:
    Given I have an existing request to create a recommendation

  @smoke
  Scenario: Dealer user party number does not match dealer code of existing recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I make a GET request as an unauthorized "dealer" user to the recommendation service's \recommendations\ recommendationNumber endpoint
    Then I expect the GET endpoint to return an 403 error code

  @smoke
  Scenario: Dealer user tries to view a recommendation created by a CAT user
    When I make a POST call to the recommendation manager's recommendations resource for "CAT"
    And I am able to retrieve that recommendation
    And I make a GET request as a "dealer" user to the recommendation service's \recommendation\recommendationNumber endpoint
    Then I expect the GET endpoint to return an 403 error code

  @med
  Scenario: Dealer User is not authorized to retrieve recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    When I make a GET request without view role to the recommendation service's \recommendations\ recommendationNumber endpoint
    Then I expect the GET endpoint to return an 403 error code