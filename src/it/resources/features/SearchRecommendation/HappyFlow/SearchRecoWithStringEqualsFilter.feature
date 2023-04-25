@301558
Feature: Feature: As a CMA in foresight, I should be able to filter recommendations with a stringEquals filter
  Background:
    Given I am a "CMA" in Foresight
    And I have an existing request to create a recommendation
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 201 response code

  @smoke
  Scenario Outline:  Successfully search with stringEquals filter
    Given I am a "CMA" in Foresight
    And I have a valid request to search recommendations with a "recommendationStatus" stringEquals filter with value "Draft"
    When I call the recommendation service POST \recommendations\search endpoint with accept header as "application/json"
      | sortBy             | orderBy | cursor                               | limit | skip | searchValue |
      | serialNumber, make | ASC     | 23da034e-a4e3-11ea-bb37-0242ac130002 | 25    | 10   | make        |
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


