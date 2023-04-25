@279684
Feature: Create Recommendation error validations

  @med
  Scenario Outline: Required fields missing
    Given I am a "CMA" in Foresight
    And I make a POST call to the recommendation manager's recommendations resource with a request missing <requiredField>
      | assetId                     | templateName        |catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue       |
      | CAT\|ERM00237\|2969484566   | Template1Default    | QPS-0001B48E    | 5000  |hours        |Asset location|Draft Recommendation|2021-01-31T22:59:59.999Z|recommendationPriority|3 - At Next Service |
    Then I expect the endpoint to return an http 400 response code
    Then I expect the endpoint to return a <message> response message

    Examples:
      |requiredField   |message                    |
      |"assetId"       |"AssetId is required"      |
      |"templateName"  |"Template name is required"|

  @med
  Scenario: Title greater than max length
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId | templateName |catrecId |reading|unitOfMeasure|site |title                                                                                                             |expirationTime |propertyName  |propertyValue |
      |         |              |         |       |             |     |DraftRecommendationDraftRecommendationDraftRecommendationDraftRecommendationDraftRecommendationDraftRecommendation|               |              |              |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

  @med
  Scenario: Asset not found
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId                   | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName  |propertyValue |
      | CAT\|00000000\|0000000000 |              |         |       |             |     |      |               |              |              |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 404 response code

  @med
  Scenario: Asset not enrolled
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId                   | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName  |propertyValue |
      | CAT\|MH400803\|4899548516 |              |         |       |             |     |      |               |              |              |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 404 response code

  @med
  Scenario: Expiration date not valid
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime           |propertyName  |propertyValue |
      |         |              |         |       |             |     |      |2027-01-31T22:59:59.999Z |              |              |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

  @med
  Scenario: estimatedRepairCost is negative
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName            |propertyValue |
      |         |              |         |       |             |     |      |               |valueEstimateRepairCost |       -1     |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

  @med
  Scenario: estimatedFailureCost is negative
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName            |propertyValue |
      |         |              |         |       |             |     |      |               |valueEstimateFailureCost|       -1     |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

  @med
  Scenario: currency is not valid
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName            |propertyValue |
      |         |              |         |       |             |     |      |               |valueEstimateCurrency   |       US     |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

  @smoke
  Scenario: Dealer user is not entitled to create recommendation
    And I have a valid request to create a recommendation
      | assetId                   | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName  |propertyValue |
      | CAT\|WSP00110\|5784585250 |              |         |       |             |     |      |               |              |              |
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    Then I expect the endpoint to return an http 403 response code

  @med
  Scenario Outline: Recommendation Action, Recommendation Details and ValueEstimateDescription contains invalid rich text
    Given I am a "CMA" in Foresight
    And I have a valid request to create a recommendation
      | assetId  | templateName |catrecId |reading|unitOfMeasure|site |title |expirationTime |propertyName        |propertyValue                                                                                                                                                              |
      |          |              |         |       |             |     |      |               |<propertyName>|<p>This is sample text to be tested</p></strong>\n<p><em><span style="text-decoration: underline;"><strong>This is what recommendation api works.</strong></span></em></p> |
    When I make a POST call to the recommendation manager's recommendations resource for "CATALL"
    Then I expect the endpoint to return an http 400 response code

    Examples:
      |propertyName              |
      |"recommendationAction"    |
      |"recommendationDetails"   |
      |"valueEstimateDescription"|