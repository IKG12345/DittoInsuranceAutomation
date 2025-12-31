package PageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ProjectLibrary.CommonUtilities;

import org.apache.logging.log4j.*;

public class UnderstandPolicyPage 
{
	private WebDriver driver;
	
	private WebDriverWait wait;
	
	private static final Logger log = LogManager.getLogger(UnderstandPolicyPage.class);
	
	/*WebElements using FindBy locators*/
	@FindBy(xpath = "//span[normalize-space() = 'Continue']/ancestor::button")
	private WebElement continueButton;
	
	
	/*WebElements using String locators*/
	private String nextButtonInSectionXpath = "//span[contains(text(), 'SECTION NAME')]/ancestor::div[contains(@class, 'mantine-Accordion-root')]/following-sibling::div/button[normalize-space() = 'Next']";
	
	private String panelXpath = "//span[contains(text(), 'PLACEHOLDER')]/ancestor::div[contains(@class, 'mantine-Accordion-item')]//div[contains(@class, 'mantine-Accordion-panel')]";
	
	
	public UnderstandPolicyPage(WebDriver driver)
	{
		this.driver = driver;	
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
	}
	
	/*
	 * Method to click on Next button in the current section.
	 * 
	 * @param currentSectionName - The name of the current section.
	 */
	public void clickNextButtonInCurrentSection(String currentSectionName)
	{
		String NextButtonXPath = nextButtonInSectionXpath.replace("SECTION NAME", currentSectionName);
		WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(NextButtonXPath)));
		
		CommonUtilities.jsScroll(nextButton, driver);
		
		nextButton.click();
	}
	
	/*
	 * Method to verify if the succeeding section is expanded.
	 * 
	 * @param succeedingSectionName - The name of the succeeding section.
	 * 
	 * @return boolean - true if the succeeding section is expanded, false otherwise.
	 */
	public boolean isSucceedingSectionExpanded(String succeedingSectionName) 
	{
		try
		{
			WebElement panel = driver.findElement(By.xpath(panelXpath.replace("PLACEHOLDER", succeedingSectionName)));
			String ariaHidden = panel.getAttribute("aria-hidden");
		
			return "false".equals(ariaHidden);
		}
		catch (Exception e) 
		{
			log.error("Unable to verify if " + succeedingSectionName + " section is expanded - " + e.getMessage());
			return false;
		}
		
	}
	
	/*
	 * Method to click on the Continue button at the end of any page.
	 * 
	 */
	public void clickContinueButton() 
	{
		if (continueButton.isDisplayed() && continueButton.isEnabled()) 
		{
			CommonUtilities.jsScroll(continueButton, driver);
			continueButton.click();
		} 
		else 
		{
			log.error("Continue button is not displayed or not enabled.");
		}
	}
	
	/*
	 * Method to verify navigation to Who Do You Want To Buy For page.
	 * 
	 * @return boolean - true if navigated to Who Do You Want To Buy For page, false otherwise.
	 */
	public boolean verifyNavigationToWhoDoYouWantToBuyForPage()
	{
		return CommonUtilities.verifyPageLoaded(driver, "Who all do you want to buy");
	}
}
