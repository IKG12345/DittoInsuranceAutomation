package StepDefinitions;

import java.util.Properties;

import org.testng.Assert;

import PageClasses.FamilyDetailsPage;
import PageClasses.HomePage;
import PageClasses.PolicyCheckoutPage;
import PageClasses.UnderstandPolicyPage;
import PageClasses.WhoIsPolicyForPage;
import ProjectLibrary.CommonUtilities;
import ProjectLibrary.ConfigReader;
import ProjectLibrary.DriverFactory;
import TestHooks.TestInitializeHooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DittoInsuranceSteps 
{
	HomePage homePage;
	ConfigReader configRead;
	Properties prop;
	
	UnderstandPolicyPage understandPolicyPage = new UnderstandPolicyPage(DriverFactory.getDriver());
	WhoIsPolicyForPage whoIsPolicyForPage = new WhoIsPolicyForPage(DriverFactory.getDriver());
	FamilyDetailsPage familyDetailsPage = new FamilyDetailsPage(DriverFactory.getDriver());
	PolicyCheckoutPage policyCheckoutPage = new PolicyCheckoutPage(DriverFactory.getDriver());
	
	private String selectedPolicyName;
	
	@Given("user is on the Ditto Insurance homepage")
	public void user_is_on_the_ditto_insurance_homepage() 
	{
		configRead = new ConfigReader();
        prop = configRead.initializeProperty();
        
        String URL = prop.getProperty("url");
        DriverFactory.getDriver().get(URL);
        CommonUtilities.waitForPageToBeLoaded(DriverFactory.getDriver(), 10);
        
        homePage = new HomePage(DriverFactory.getDriver());
		TestInitializeHooks.reportPass("Navigated to Ditto Insurance homepage successfully");
	}
	
	@And("user scrolls to the {string} section")
	public void user_scrolls_to_the_section(String sectionName) 
	{
		try
		{
			homePage.scrollToSection(sectionName);
			TestInitializeHooks.reportPass("Scrolled to " + sectionName + " section successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to scroll to " + sectionName + " section - " + e.getMessage());
		}
	}
	
	@When("user clicks on {string} card")
	public void user_selects_the_product(String productName) 
	{
		this.selectedPolicyName = productName;
		
		try 
		{
			homePage.selectInsurance(productName);
			TestInitializeHooks.reportPass("Selected " + productName + " product successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to select " + productName + " product - " + e.getMessage());
		}
	}
	
	@Then("the Understand your policy page should be displayed")
	public void the_page_should_be_displayed() 
	{
		boolean isUnderstandPolicy = homePage.verifyUnderstandPolicyPage();
		
		Assert.assertTrue(isUnderstandPolicy);
		TestInitializeHooks.reportPass("Understand your policy page is displayed successfully");
		
	}
	
	@When("user clicks Next in the {string} section")
	public void user_clicks_next_in_current_section(String currentSectionName)
	{
		try
		{
			understandPolicyPage.clickNextButtonInCurrentSection(currentSectionName);
			TestInitializeHooks.reportPass("Clicked Next button in " + currentSectionName + " section successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to click Next button in " + currentSectionName + " section - " + e.getMessage());
		}
	}
	
	@Then("the {string} section should be expanded")
	public void the_next_section_should_be_expanded(String succeedingSectionName) 
	{
		try
		{
			CommonUtilities.waitForSpecificTime(DriverFactory.getDriver(), 5);
			boolean isExpanded = understandPolicyPage.isSucceedingSectionExpanded(succeedingSectionName);
			
			if (isExpanded) 
			{
				TestInitializeHooks.reportPass(succeedingSectionName + " section is expanded successfully");
			} 
			else 
			{
				TestInitializeHooks.reportFail(succeedingSectionName + " section is not expanded");
			}
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to verify if " + succeedingSectionName + " section is expanded - " + e.getMessage());
		}
	}
	
	@When("user clicks on the Continue button")
	public void user_clicks_on_the_continue_button() 
	{
		try
		{
			understandPolicyPage.clickContinueButton();
			TestInitializeHooks.reportPass("Clicked Continue button successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to click Continue button - " + e.getMessage());
		}
	}
	
	@Then("user should navigate to Who do you want to buy this insurance for? page")
	public void user_should_navigate_to_who_to_buy_for_page() 
	{
		try
		{
			boolean isOnExpectedPage = understandPolicyPage.verifyNavigationToWhoDoYouWantToBuyForPage();
			
			if(isOnExpectedPage)
			{
				TestInitializeHooks.reportPass("Navigated to Who do you want to buy this insurance for? page successfully");
			}
		}
		catch(Exception e)
		{
			TestInitializeHooks.reportFail("Failed to navigate to Who do you want to buy this insurance for? page - " + e.getMessage());
		}
	}
	
	@When("user selects {string} with gender {string}")
    public void user_selects_member_with_gender(String memberName, String gender) 
	{
        try 
        {
        	whoIsPolicyForPage.selectSelectHolderAndGender(memberName, gender);           
            TestInitializeHooks.reportPass("Selected Member: " + memberName + " | Gender: " + gender);
        }
        catch (Exception e) 
        {
            TestInitializeHooks.reportFail("Failed to select " + memberName + " (" + gender + ") - " + e.getMessage());
        }
    }
	
	@And("user clicks on the Next step button")
	public void user_clicks_on_the_next_step_button()
	{
		try 
		{
			whoIsPolicyForPage.clickNextStepButton();
			TestInitializeHooks.reportPass("Clicked Next step button successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to click Next step button - " + e.getMessage());
		}
	}
	
	@Then("user should navigate to Tell us about your family page")
	public void user_should_navigate_to_tell_us_about_your_family_page()
	{
		try
		{
			whoIsPolicyForPage.verifyNavigationToTellUsAboutYourFamiliyPage();
			TestInitializeHooks.reportPass("Navigated to Tell us about your familiy page successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to navigate to Tell us about your familiy page - " + e.getMessage());
		}
	}
	
	@When("user fills their {string}, {string} and {string}")
	public void user_fills_their_family_details(String selfAge, String spouseAge, String pincode)
	{
		try
		{
			familyDetailsPage.enterFamilyDetails(selfAge, spouseAge, pincode);
			CommonUtilities.waitForSpecificTime(DriverFactory.getDriver(), 11);
			TestInitializeHooks.reportPass("Entered family details successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to enter family details - " + e.getMessage());
		}
	}
	
	@When("user selects the policy cover amount as {string}")
	public void user_selects_the_policy_cover_amount(String coverAmount) 
	{
		familyDetailsPage.setPolicyName(this.selectedPolicyName);
		
		try
		{
			familyDetailsPage.setCoverAmount(coverAmount);
			TestInitializeHooks.reportPass("Set cover amount to " + coverAmount + " successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to set cover amount to " + coverAmount + " - " + e.getMessage());
		}
	}
	
	@When("user clicks on the Calculate Premium button")
	public void user_clicks_on_the_calculate_premium_button() 
	{
		try
		{
			familyDetailsPage.clickCalculatePremiumButton();
			TestInitializeHooks.reportPass("Clicked Calculate Premium button successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to click Calculate Premium button - " + e.getMessage());
		}
	}
	
	@Then("AddOns section should appear")
	public void addons_section_should_appear() 
	{
		try
		{
			familyDetailsPage.verifyAddOnsRidersSectionAppears();
			TestInitializeHooks.reportPass("AddOns section appeared successfully");
		}
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("AddOns section did not appear - " + e.getMessage());
		}
	}
	
	@Then("user selects {string} add-on from Recommended Addons")
	public void user_selects_addon_from_recommended_addons(String recommendedAddOnName)
	{
		try
		{
			familyDetailsPage.handleAddOns(recommendedAddOnName);
			TestInitializeHooks.reportPass("Selected " + recommendedAddOnName + " add-on successfully");
		}
		catch(Exception e)
		{
			TestInitializeHooks.reportFail("Failed to select " + recommendedAddOnName + " add-on - " + e.getMessage());
		}
	}
	
	@And("user selects {string} from Other Addons")
	public void user_selects_from_other_addons(String otherAddOnName) 
	{
		try 
		{
			familyDetailsPage.handleOtherAddOns(otherAddOnName);
			TestInitializeHooks.reportPass("Selected " + otherAddOnName + " add-on successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to select " + otherAddOnName + " add-on - " + e.getMessage());
		}
	}
	
	@And("user clicks the Buy this policy button")
	public void user_clicks_the_buy_this_policy_button() 
    {
		try 
		{
			familyDetailsPage.clickBuyThisPolicyButton();
			TestInitializeHooks.reportPass("Clicked Buy this policy button successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to click Buy this policy button - " + e.getMessage());
		}
    }
	
	@Then("user should navigate to the Policy checkout page")
	public void user_should_navigate_to_the_policy_checkout_page() 
	{
		try 
		{
			familyDetailsPage.verifyPolicyCheckoutPage();
			TestInitializeHooks.reportPass("Navigated to Policy checkout page successfully");
		} 
		catch (Exception e) 
		{
			TestInitializeHooks.reportFail("Failed to navigate to Policy checkout page - " + e.getMessage());
		}
	}
	
    @Then("user verifies the total policy premium is the addition of base premium and add ons")
    public void the_total_premium_should_be_the_adiition_of_base_premium_and_add_ons() 
    {
    	boolean isPremiumCalculationCorrect = policyCheckoutPage.verifyPolicyPremiumCalculation();
    	
    	if(isPremiumCalculationCorrect)
    		TestInitializeHooks.reportPass("Policy premium calculation is correct");
    	else
    	{
    		TestInitializeHooks.reportFail("Policy premium calculation is incorrect");
    		Assert.fail("Policy premium calculation is incorrect");
    	}
    }
}
