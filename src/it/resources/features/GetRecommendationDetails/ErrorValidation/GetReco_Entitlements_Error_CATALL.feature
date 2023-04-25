@279662
Feature: Get Recommendation error validations

  @med
  Scenario: Recommendation does not exist
    Given I am a "CMA" in Foresight
    When I make a GET request as a "CATALL" user to the recommendation service's \recommendation\ "REC-000-000-000" endpoint
    Then I expect the endpoint to return an http 404 response code