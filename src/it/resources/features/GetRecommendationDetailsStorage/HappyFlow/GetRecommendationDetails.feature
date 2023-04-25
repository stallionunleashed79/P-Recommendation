@270716
Feature: As a foresight user, when I retrieve a recommendation's details, I need to see the details of the recommendation's attachments in the response
  Background:
    Given I have an existing request to create a recommendation

  @med
  Scenario: Map storage service response to response body as CATALL
    Given I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    And I am able to retrieve that recommendation
    And I am a "CMA" in Foresight
    And I make a GET request as a "CATALL" user to the recommendation service's \recommendation\recommendationNumber endpoint
    Then I expect the endpoint to return an http 200 response code
#    And I expect each object in "attachments" array to contain "name"
#    And I expect each object in "attachments" array to contain "fileId"
#    And I expect each object in "attachments" array to contain "fileAttachedTime"

  @med
  Scenario: Map storage service response to response body as a dealer
    Given I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I am a "CMA" in Foresight
    And I make a GET request as a "dealer" user to the recommendation service's \recommendation\recommendationNumber endpoint
    Then I expect the endpoint to return an http 200 response code
#    And I expect each object in "attachments" array to contain "name"
#    And I expect each object in "attachments" array to contain "fileId"
#    And I expect each object in "attachments" array to contain "fileAttachedTime"

  @med
  Scenario: Map storage service response to response body as a CAT user
    Given I make a POST call to the recommendation manager's recommendations resource for "CAT"
    And I am able to retrieve that recommendation
    And I am a "CMA" in Foresight
    And I make a GET request as a "CAT" user to the recommendation service's \recommendation\recommendationNumber endpoint
    Then I expect the endpoint to return an http 200 response code
#    And I expect each object in "attachments" array to contain "name"
#    And I expect each object in "attachments" array to contain "fileId"
#    And I expect each object in "attachments" array to contain "fileAttachedTime"
