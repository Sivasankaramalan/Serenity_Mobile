Feature: Verify Onboarding of Act Application

  @smoke
  Scenario: Verify Onboarding
    Given I am launching the application
    And I should verify the onboarding screen
    When I click on the "Register for new connection" button
    And I should verify the onboarding screen
    And I enter the "Full Name" as "John"
    And I enter the "Mobile Number" as "9000000000"
    And I select city as "Hyderabad"
    When I click on the "Continue" button






#    Then I should see the "Existing Account" screen
#    And I should verify the "Existing Account" screen
#    And I click on the "Login" button
#    And I should verify the "Login" screen
#    And I enter the "Mobile Number" as "9000000000"
#    And I click on the "Login" button
#