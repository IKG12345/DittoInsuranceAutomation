package PageClasses;

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

public class WhoIsPolicyForPage 
{
	private WebDriver driver;
	
	private WebDriverWait wait;
	
	private static final Logger log = LogManager.getLogger(WhoIsPolicyForPage.class);
	
	/*WebElements using FindBy locators*/
	@FindBy(xpath = "//span[text() = 'Next step']/ancestor::button")
	private WebElement nextStepButton;
	
	/*WebElements using String locators*/
    private String memberLabelXpath = "//span[contains(text(), '%s')]/ancestor::label[contains(@class, 'mantine-Chip-label')]";

    private String memberGenderXpath = "//span[contains(text(), '%s')]/ancestor::label[contains(@class, 'mantine-Chip-label')]//div[contains(text(), '%s')]";
	
	
	public WhoIsPolicyForPage(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
	}
	
	/*
	 * Method to click on the policy holder under "Select all your family members you want to insure" section.
	 * 
	 * @param policyHolder - The name of person who is purchasing the policy.
	 * 
	 * @param Gender - The gender of that person.
	 * */
	public void selectSelectHolderAndGender(String policyHolder, String Gender)
	{
		String labelXpath = String.format(memberLabelXpath, policyHolder);
		WebElement memberCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(labelXpath)));
		memberCard.click();
		log.info("Selected policy holder: " + policyHolder);
		
		String genderXpath = String.format(memberGenderXpath, policyHolder, Gender);
		WebElement genderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(genderXpath)));
		genderBtn.click();
		log.info("Selected gender of policy holder: " + Gender);
	}
	
	/*
	 * Method to click on Next step button.
	 * 
	 */
	public void clickNextStepButton()
	{
		CommonUtilities.jsScroll(nextStepButton, driver);
		
		wait.until(driver -> {String disabledAttr = nextStepButton.getAttribute("disabled");return disabledAttr == null;});
		wait.until(ExpectedConditions.elementToBeClickable(nextStepButton));
		
		nextStepButton.click();
		log.info("Clicked Next step button successfully");
	}
	
	/*
	 * Method to verify navigation to Tell us about your family page.
	 * 
	 * @return boolean - true if navigated to Tell us about your family page, false otherwise.
	 */
	public boolean verifyNavigationToTellUsAboutYourFamiliyPage()
	{
		return CommonUtilities.verifyPageLoaded(driver, "Tell us about your family");
	}
		
}
