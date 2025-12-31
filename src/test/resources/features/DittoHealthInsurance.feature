#Author: indranil.kargupta@outlook.com

	Feature: Verify Ditto Health Insurance user flow
  
	Background:
	Given user is on the Ditto Insurance homepage

  	@SC01 @DittoInsurance @HealthInsurance @End-to-endFlow
  	Scenario Outline: Verify user can select any health insurance provider of choice and checkout
    And user scrolls to the "Health" section
    When user clicks on "<Insurance product>" card
    Then the Understand your policy page should be displayed
    When user clicks Next in the "Main Benefits" section
		Then the "Waiting Periods" section should be expanded
    When user clicks Next in the "Waiting Periods" section
    Then the "Whats Not Covered" section should be expanded
    When user clicks Next in the "Whats Not Covered" section
    Then the "Extra Benefits" section should be expanded
    When user clicks on the Continue button
    Then user should navigate to Who do you want to buy this insurance for? page
    When user selects "Self" with gender "Male"
    And user selects "Spouse" with gender "Female"
    And user clicks on the Next step button
    Then user should navigate to Tell us about your family page
    When user fills their "<Self age>", "<Spouse age>" and "<Pincode>"
    And user selects the policy cover amount as "<Policy Cover Amount>"
    And user clicks on the Calculate Premium button
    Then AddOns section should appear
    And user selects "<Add On>" add-on from Recommended Addons
    And user selects "<Additional Add On>" from Other Addons
    And user clicks the Buy this policy button
    Then user should navigate to the Policy checkout page
    Then user verifies the total policy premium is the addition of base premium and add ons

     Examples: 
    	
     | Insurance product | Self age | Spouse age | Pincode | Policy Cover Amount | Add On 							 | Additional Add On     |
     | Optima Secure     | 28       | 26         | 560030  | 20 L				 				 | ABCD  							   | Limitless				     |
     | Care Supreme		   | 30       | 28         | 110001  | 50 L								 | Annual Health Checkup | Unlimited Care 			 |
     