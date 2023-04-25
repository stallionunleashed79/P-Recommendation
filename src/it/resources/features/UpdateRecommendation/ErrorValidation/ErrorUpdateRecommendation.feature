@301645
Feature: Update Recommendation - Error Scenarios
  Background:
    Given I have an existing request to create a recommendation

    @smoke
  Scenario Outline: Dealer user tries to update recommendation created by a CATALL or CAT users
    When I make a POST call to the recommendation manager's recommendations resource for <userType>
    And I am able to retrieve that recommendation
    And I have an existing request to update a recommendation
    When I make a PUT call to the recommendation manager's recommendations resource as "dealer"
    Then I expect the endpoint to return an http 403 response code

      Examples:
        |userType   |
        |"CATALL"   |
        |"CAT"      |

  @smoke
  Scenario: CAT user tries to update recommendation with partyNumbers mismatch
    When I make a POST call to the recommendation manager's recommendations resource for "CAT"
    And I am able to retrieve that recommendation
    And I have an existing request to update a recommendation
    When I make a PUT call to the recommendation manager's recommendations resource as an unauthorized "CAT"
    Then I expect the endpoint to return an http 403 response code

  @smoke
  Scenario: Dealer user tries to update recommendation with partyNumbers mismatch
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I have an existing request to update a recommendation
    When I make a PUT call to the recommendation manager's recommendations resource as an unauthorized "dealer"
    Then I expect the endpoint to return an http 403 response code

  @smoke
  Scenario: CAT user tries to update recommendation created by a dealer user
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I have an existing request to update a recommendation
    When I make a PUT call to the recommendation manager's recommendations resource as "CAT"
    Then I expect the endpoint to return an http 403 response code

  @smoke
  Scenario: CATALL user tries to update recommendation created by a dealer user
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I have an existing request to update a recommendation
    When I make a PUT call to the recommendation manager's recommendations resource as "CATALL"
    Then I expect the endpoint to return an http 403 response code

  @med
  Scenario Outline: Recommendation status and priority missing in request body during update
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I make a PUT call to the recommendation manager's recommendations resource with a request missing <requiredField>
      | catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue  |
      | QPS-0001B48E     | 3000  |hours        |Asset location|some title          |2021-01-31T22:59:59.999Z|recommendationStatus  |Expired|
    Then I expect the endpoint to return an http 400 response code
    Then I expect the endpoint to return a <message> response message

    Examples:
      |requiredField   |          message             |
      |"recommendationStatus"  |"The recommendationStatus is a required field." |
      |"recommendationPriority"  |"The recommendationPriority is a required field." |

  @smoke
  Scenario Outline: Dealer user tried to update recommendation details, recommendation action and value estimate description with invalid rich text input
    When I make a POST call to the recommendation manager's recommendations resource for "dealer"
    And I am able to retrieve that recommendation
    And I have a valid request to update a recommendation
      | catrecId         |reading|unitOfMeasure|site          |title               |expirationTime          |propertyName          |propertyValue  |secondPropertyName          |secondPropertyValue  |
      | QPS-0001B48E     | 3000  |hours        |Asset location|some title          |2021-01-31T22:59:59.999Z|recommendationStatus  |Expired|<propertyName>  |<p>This is sample text to be tested</p></strong>\n<p><em><span style="text-decoration: underline;"><strong>This is what recommendation api works.</strong></span></em></p>|
    When I make a PUT call to the recommendation manager's recommendations resource as "dealer"
    Then I expect the endpoint to return an http 400 response code

    Examples:
      |propertyName                |
      |"recommendationDetails"     |
      |"recommendationAction"      |
      |"valueEstimateDescription"  |


