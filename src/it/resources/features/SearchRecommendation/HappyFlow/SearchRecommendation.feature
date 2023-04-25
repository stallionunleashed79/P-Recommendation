@301558
Feature: As a CMA in Foresight, I need to retrieve the recommendation summary module
  Background:
    Given I am a "CMA" in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code

  @smoke
   Scenario Outline: Successful retrieval of recommendations summary list
    Given I am a "CMA" in Foresight
    When I call the recommendation service POST \recommendations\search endpoint
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC, DESC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
    Then I expect the endpoint to return an http 200 response code
    And I expect the endpoint to return object "recommendations" with recommendations array
    And I expect "recommendations" object to contain <field> field
    And I expect each recommendation within "recommendations" to contain an "owner" object object with "catrecid" of the recommendation owner
    And I expect each recommendation within "recommendations" to contain an "owner" object object with "firstName" of the recommendation owner
    And I expect each recommendation within "recommendations" to contain an "owner" object object with "lastName" of the recommendation owner

    Examples:
      | field                    |
      | "serialNumber"           |
      | "make"                   |
      | "model"                  |
      | "createdTime"            |
      | "title"                  |
      | "recommendationPriority" |
      | "recommendationStatus"   |
      | "expirationTime"         |
      | "ucidName"               |
      | "site"                   |
      | "assetId"                |
      | "recommendationNumber"   |
      | "templateName"           |
      | "owner"                  |
#      | "workOrderNumber"      |
