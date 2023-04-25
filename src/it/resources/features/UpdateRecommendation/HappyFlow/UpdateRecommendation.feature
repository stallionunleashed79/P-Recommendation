@273489
Feature: Update Recommendation - Happy Flow

    @smoke
  Scenario Outline: CATALL, CAT and Dealer users can update a recommendation created by themselves
    And I have a valid request to create a recommendation
      | assetId                     | templateName             |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue      |
      | CAT\|ERM00237\|2969484566   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service|
    When I make a POST call to the recommendation manager's recommendations resource for <userTypeForCreate>
    And I am able to retrieve that recommendation
    And I have a valid request to update a recommendation
      | catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue  |secondPropertyName          |secondPropertyValue  |
      | QPS-0001B48E     | 3000  |hours        |Asset location|some title          |2021-01-31T22:59:59.999Z|recommendationStatus  |Expired|recommendationPriority  |3 - At Next Service|
    When I make a PUT call to the recommendation manager's recommendations resource as <userTypeForUpdate>
    Then I expect the endpoint to return an http 200 response code
    Then I expect the response body to contain a "recommendationNumber" field
    Then I expect the response body to contain a "commonFields" field
    Then I expect the response body to contain a "assetId" field
    Then I expect the response body to contain a "templateName" field
    Then I expect the response body to contain a "templateCustomProperties" field
    Then I expect the response body to not contain a "links" field
    Then I expect the response body to not contain a "attachments" field
    Then I expect the "assetId" property in the response body to contain "CAT|ERM00237|2969484566"
    Then I expect the "templateName" property in the response body to contain "Template1Default"
    Then I expect the "commonFields.owner.catrecid" property in the response body to contain "QPS-0001B48E"
    Then I expect the "commonFields.hoursReading.reading" property in the response body to contain "3000"
    Then I expect the "commonFields.site" property in the response body to contain "Asset location"
    Then I expect the "templateCustomProperties" property in the response body to contain "recommendationStatus"
    Then I expect the "templateCustomProperties" property in the response body to contain "Expired"
    Then I expect the "templateCustomProperties" property in the response body to contain "recommendationPriority"
    Then I expect the "templateCustomProperties" property in the response body to contain "3 - At Next Service"

    Examples:
      |userTypeForCreate|userTypeForUpdate|
      |"CATALL"         |"CATALL"         |
      |"CAT"            |"CAT"            |
      |"dealer"         |"dealer"         |

    @smoke
  Scenario Outline: Dealer user successfully updates recommendation action with rich text input in recommendationAction,recommendationDetails and valueEstimateDescription
    And I have a valid request to create a recommendation
      | assetId                     | templateName             |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue      |
      | CAT\|ERM00237\|2969484566   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service|
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I have a valid request to update a recommendation
      | catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue  |secondPropertyName| secondPropertyValue| thirdPropertyName          |thirdPropertyValue  |
      | QPS-0001B48E     | 3000  |hours        |Asset location|some title          |2021-01-31T22:59:59.999Z|recommendationStatus  |Expired|recommendationPriority    |3 - At Next Service | <propertyName>  |<propertyValue>|
    When I make a PUT call to the recommendation manager's recommendations resource as "dealer"
    Then I expect the endpoint to return an http 200 response code
    Then I expect the response body to contain a "recommendationNumber" field
    Then I expect the response body to contain a "commonFields" field
    Then I expect the response body to contain a "assetId" field
    Then I expect the response body to contain a "templateName" field
    Then I expect the response body to contain a "templateCustomProperties" field
    Then I expect the response body to not contain a "links" field
    Then I expect the response body to not contain a "attachments" field
    Then I expect the "assetId" property in the response body to contain "CAT|ERM00237|2969484566"
    Then I expect the "templateName" property in the response body to contain "Template1Default"
    Then I expect the "commonFields.owner.catrecid" property in the response body to contain "QPS-0001B48E"
    Then I expect the "commonFields.hoursReading.reading" property in the response body to contain "3000"
    Then I expect the "commonFields.site" property in the response body to contain "Asset location"
    Then I expect the "templateCustomProperties" property in the response body to contain "recommendationStatus"
    Then I expect the "templateCustomProperties" property in the response body to contain "Expired"
    Then I expect the "templateCustomProperties" property in the response body to contain "recommendationPriority"
    Then I expect the "templateCustomProperties" property in the response body to contain "3 - At Next Service"

    Examples:
        |propertyName             |propertyValue                                           |
        |recommendationAction     |<p><strong>This is sample text to be tested</p></strong>|
        |recommendationDetails    |<p><strong>This is sample text</p></strong>             |
        |valueEstimateDescription |<p><strong>This is sample text to be tested</p></strong>\n<p><em><span style="text-decoration: underline;"><strong>This is what recommendation api works.</strong></span></em></p>|
