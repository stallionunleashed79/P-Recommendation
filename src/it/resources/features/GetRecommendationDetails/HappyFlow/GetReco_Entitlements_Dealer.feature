@292114
Feature: As a dealer user i want to be able to successfully retrieve existing recommendations in Foresight

  Background:
    Given I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation

    @smoke
    Scenario: Dealer user retrieves an existing recommendation
      Given I am a dealer user in Foresight
      When I make a GET request as a "dealer" user to the recommendation service's \recommendation\recommendationNumber endpoint
      And I expect the response body to contain a "recommendationNumber" field
      Then I expect the endpoint to return an http 200 response code