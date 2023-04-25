Feature: HealthCheck
  @smoke
  Scenario: User perform a Health check to the application and check response is successful
    When a user performs a Health check to the application
    Then I expect the endpoint to return an http 200 response code