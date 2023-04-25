@279662
Feature: As a CATALL user I want the ability to successfully retrieve existing recommendations in Foresight

  Background:
    Given I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    And I am able to retrieve that recommendation

  @smoke
  Scenario: User retrieves an existing recommendation
    Given I am a "CMA" in Foresight
    When I make a GET request as a "CATALL" user to the recommendation service's \recommendation\recommendationNumber endpoint
    And I expect the response body to contain a "recommendationNumber" field
    Then I expect the endpoint to return an http 200 response code

  @high
  Scenario: Response adheres to correct schema
    Given I make a GET request as a "CATALL" user to the recommendation service's \recommendation\recommendationNumber endpoint
    Then I expect this "commonFields" object to contain "site"
    Then I expect this "commonFields" object to contain "assetOwnershipAtRecommendation"
    Then I expect this "assetOwnershipAtRecommendation" object to contain "dealerCode"
    Then I expect this "assetOwnershipAtRecommendation" object to contain "dealerName"
#    Then I expect this "assetOwnershipAtRecommendation" object to contain "ucid"
#    Then I expect this "assetOwnershipAtRecommendation" object to contain "ucidName"
    Then I expect this "commonFields" object to contain "assetMetadata"
    Then I expect this "assetMetadata" object to contain "serialNumber"
    Then I expect this "assetMetadata" object to contain "make"
    Then I expect this "assetMetadata" object to contain "model"
    Then I expect this "assetMetadata" object to contain "name"
#    Then I expect this "assetMetadata" object to contain "iconUrl"
#    Then I expect this "assetMetadata" object to contain "productFamily"
#    Then I expect this "productFamily" object to contain "code"
#    Then I expect this "productFamily" object to contain "name"
#    Then I expect this "productFamily" object to contain "iconUrl"
    Then I expect the response body to contain a "templateCustomProperties" field
    Then I expect each object in "templateCustomProperties" array to contain "propertyName"
    Then I expect each object in "templateCustomProperties" array to contain "propertyValue"
#    Then I expect each object in "attachments" array to contain "fileId"
#    Then I expect each object in "attachments" array to contain "name"
#    Then I expect each object in "attachments" array to contain "fileAttachedTime"
#    Then I expect each object in "links" array to contain "attachedDate"
#    Then I expect each object in "links" array to contain "url"
#    Then I expect the response body to contain a "exceptions" field
#    Then I expect the response body to contain a "events" field


