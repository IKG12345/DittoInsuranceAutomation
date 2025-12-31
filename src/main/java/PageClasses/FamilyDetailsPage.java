package PageClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ProjectLibrary.CommonUtilities;

public class FamilyDetailsPage 
{
	private WebDriver driver;
	
	private WebDriverWait wait;
	
	private Logger log = LogManager.getLogger(FamilyDetailsPage.class);
	
	private List<String> coverAmounts = new ArrayList<String>();
	
	/*WebElements using FindBy locators*/
	@FindBy(xpath = "//input[@name = 'Selfage']")
	private WebElement yourAgeInput;
	
	@FindBy(xpath = "//input[@name = 'Spouseage']")
	private WebElement spouseAgeInput;
	
	@FindBy(xpath = "//input[@name = 'pincode']")
	private WebElement pincodeInput;
	
	@FindBy(xpath = "//div[contains(@class, '_coverChangeBox')]/span[contains(@class, 'mantine-Text-root')]")
	private WebElement coverAmountText;
	
	@FindBy(xpath = "//div[contains(@class, '_coverChangeBox')]/button[position() = 1]")
	private WebElement minusBtn;
	
	@FindBy(xpath = "//div[contains(@class, '_coverChangeBox')]/button[position() = 2]")
	private WebElement plusBtn;
	
	@FindBy(xpath = "//div[@class='m_4081bf90 mantine-Group-root mantine-visible-from-sm']//button[@type='submit']")
	private WebElement calculatePremiunBtn;
	
	@FindBy(xpath = "//button[contains(@id, '-control-addons')]/descendant::span[contains(text(), 'Add Ons & Riders')]")
	private WebElement addOnsRidersSectionHeader;
	
	@FindBy(xpath = "//section[@role='dialog' and contains(@class, 'mantine-Modal-content')]")
	private WebElement abcdDialogBox;
	
	@FindBy(xpath = "//section[@role='dialog']/descendant::label[contains(text(),'Self')]/parent::div/preceding-sibling::div/child::input")
	private WebElement abcdSelfCheckbox;
	
	@FindBy(xpath = "//span[normalize-space() = 'Confirm']/ancestor::button")
	private WebElement confirmBtn;
	
	@FindBy(xpath = "//button[contains(@class, '_othersAccordionControl')]")
	private WebElement othersAddOnsAccordion;
	
	@FindBy(xpath = "//span[contains(text(), 'Buy this policy')]/ancestor::button")
	private WebElement buyThisPolicyBtn;
	
	@FindBy(xpath = "//div[contains(@class, 'mantine-Slider-markLabel') and starts-with(text(), '₹')]")
	private List<WebElement> policyCoverAmountSlabs;
	
	/*WebElements using String locators*/
	private String policyName = " ";
	
	private String sliderDotXpath = "//div[contains(@class, 'mantine-Slider-markLabel') and contains(text(), '$')]/preceding-sibling::div[contains(@class, 'mantine-Slider-mark')]";
	
	private String addOnCheckboxXpath = "//input[contains(@class, 'mantine-Checkbox-input') and @name = '#']";
	
	
	public FamilyDetailsPage(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
	}
	
	public void setPolicyName(String policyName) 
	{
        this.policyName = policyName;
    }
	
	/*
	 * Method to enter family details like your age, spouse age and pincode.
	 * 
	 * @param yourAge - The age of the policyholder.
	 * 
	 * @param spouseAge - The age of the spouse.
	 * 
	 * @param pincode - The pincode of the residence.
	 */
	public void enterFamilyDetails(String yourAge, String spouseAge, String pincode) 
	{
		yourAgeInput.clear();
		yourAgeInput.sendKeys(yourAge);
		log.info("Entered your age: " + yourAge);

		spouseAgeInput.clear();
		spouseAgeInput.sendKeys(spouseAge);
		log.info("Entered spouse age: " + spouseAge);

		pincodeInput.clear();
		pincodeInput.sendKeys(pincode);
		log.info("Entered pincode: " + pincode);
	}
	
	/*
	 * Method to fetch policy cover slabs dynamically from the UI.
	 * 
	 * @return List of cover slabs as strings.
	 */
	public List<String> fetchPolicyCoverSlabs()
	{
		List<String> dynamicCoverSlabs = new ArrayList<String>();
		
		for(WebElement slab : policyCoverAmountSlabs)
		{
			String slabText = slab.getText().replace("₹","").trim();
			dynamicCoverSlabs.add(slabText);
		}
		
		log.info("Fetched cover slabs: " + dynamicCoverSlabs + "for policy: " + policyName);
		return dynamicCoverSlabs;
	}
	
	/*
	 * Method to set the cover amount.
	 * 
	 * @param targetAmount - The desired cover amount.
	 */
	public void setCoverAmount(String targetAmount)
	{
		this.coverAmounts = fetchPolicyCoverSlabs();
		
		if (!coverAmounts.contains(targetAmount)) 
		{
			throw new IllegalArgumentException("Invalid cover amount: " + targetAmount);
		} 
		
		String uiText = wait.until(ExpectedConditions.visibilityOf(coverAmountText)).getText();
		String currentValue = uiText.replace("₹", "").trim();
		
		int currentIndex = coverAmounts.indexOf(currentValue);
		int targetIndex = coverAmounts.indexOf(targetAmount);
		
		int clicksNeeded = targetIndex - currentIndex;
		
		if(clicksNeeded > 0)
		{
			for(int i = 0; i < clicksNeeded; i ++)
			{
				plusBtn.click();
				CommonUtilities.waitForSpecificTime(driver, 2);
			}
			log.info("Increased cover amount by " + clicksNeeded + " clicks." + " Cover amount set to: " + targetAmount);
		}
		else if(clicksNeeded < 0)
		{
			for(int i = 0; i < Math.abs(clicksNeeded); i ++)
			{
				minusBtn.click();
				CommonUtilities.waitForSpecificTime(driver, 2);
			}
		}
		
		uiText = wait.until(ExpectedConditions.visibilityOf(coverAmountText)).getText();
	    String finalValue = uiText.replace("₹", "").trim();
	    
	    log.info("Cover amount set to: " + finalValue);
	    
		if (!finalValue.equals(targetAmount)) 
		{
			throw new RuntimeException("Failed to set cover amount. Expected: " + targetAmount + ", Found: " + currentValue);
		}
	}
	
	/*
	 * Method to set the cover amount using slider.
	 * 
	 * @param targetAmount - The desired cover amount.
	 */
	public void setCoverAmountOnslider(String targetAmount)
	{
		WebElement sliderDot = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(sliderDotXpath.replace("$", targetAmount))));
		CommonUtilities.jsClick(sliderDot, driver);
	}
	
	/*
	 * Method to click on Calculate Premium button.
	 * 
	 */
	public void clickCalculatePremiumButton() 
	{
		if (calculatePremiunBtn.isDisplayed() || !calculatePremiunBtn.isEnabled()) 
		{
			calculatePremiunBtn.click();
			log.info("Clicked Calculate Premium button.");
		}
	}
	
	/*
	 * Method to verify Add Ons & Riders section appears.
	 * 
	 * @return true if the section appears, false otherwise.
	 */
	public boolean verifyAddOnsRidersSectionAppears()
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(addOnsRidersSectionHeader));
			log.info("Add Ons & Riders section is displayed.");
			return true;
		} 
		catch (Exception e) 
		{
			log.error("Add Ons & Riders section is not displayed.");
			return false;
		}
	}
	
	/*
	 * Method to handle Add Ons selection.
	 * 
	 * @param addOnName - The name of the add-on to select.
	 * 
	 */
	public void handleAddOns(String addOnName)
	{
		if(addOnName.equalsIgnoreCase("ABCD"))
		{			
			WebElement addOnCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addOnCheckboxXpath.replace("#", addOnName))));
			addOnCheckbox.click();
			
			wait.until(ExpectedConditions.visibilityOf(abcdDialogBox));
			wait.until(ExpectedConditions.elementToBeClickable(abcdSelfCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(confirmBtn)).click();
			CommonUtilities.waitForSpecificTime(driver, 8);
			
			log.info("Selected add-on: " + addOnName + " and self in ABCD dialog box.");
		}
		else
		{
			WebElement addOnCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addOnCheckboxXpath.replace("#", addOnName))));
			addOnCheckbox.click();
			
			log.info("Selected add-on: " + addOnName);
		}
	}
	
	/*
	 * Method to handle other Add Ons selection.
	 * 
	 * @param additionalAddOnName - The name of the additional add-on to select.
	 * 
	 */
	public void handleOtherAddOns(String additionalAddOnName)
	{
		if (othersAddOnsAccordion.getAttribute("aria-expanded").equals("false")) 
			othersAddOnsAccordion.click();

		WebElement additionalAddOnCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addOnCheckboxXpath.replace("#", additionalAddOnName))));
		additionalAddOnCheckbox.click();
		
		CommonUtilities.waitForSpecificTime(driver, 8);
		
		log.info("Selected additional add-on: " + additionalAddOnName);
	}
	
	/*
	 * Method to click on Buy This Policy button.
	 * 
	 */
	public void clickBuyThisPolicyButton()
	{
		if (buyThisPolicyBtn.isDisplayed() || !buyThisPolicyBtn.isEnabled()) 
		{
			buyThisPolicyBtn.click();
			log.info("Clicked Buy This Policy button.");
		}
	}
	
	/*
	 * Method to verify navigation to Policy Checkout Page.
	 * 
	 * @return true if navigated successfully, false otherwise.
	 */
	public boolean verifyPolicyCheckoutPage() 
	{
		return CommonUtilities.verifyPageLoaded(driver, "Policy checkout summary");
	}
	
}
