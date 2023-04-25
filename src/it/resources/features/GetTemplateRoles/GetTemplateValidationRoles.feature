@308475
Feature: As a foresight user, I need the recommendation manager to validate my authorization so I can only retrieve recommendations template when I have view permissions in the recommendations domain

  Scenario: User is not authorized to retrieve recommendation template
    Given I am a "user" in Foresight
    When I send a GET request to the recommendation manager's \templates\ "Template1Default" endpoint with role "DEALERWITHOUTROLES"
    Then I expect the endpoint to return an http 403 response code
    And I expect the error message to be "Forbidden, User doesn't have permissions to perform the operation"

  Scenario: User is authorized to retrieve recommendation template
    Given I am a "user" in Foresight
    When I send a GET request to the recommendation manager's \templates\ "Template1Default" endpoint
    Then I expect the endpoint to return an http 200 response code
    And I look at the "Recommendation" section of the response