package PageClasses;

import java.time.Duration;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ProjectLibrary.CommonUtilities;

import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class HomePage 
{
	// WebDriver instance
	private WebDriver driver;
	
	// WebDriverWait instance
	private WebDriverWait wait;
	
	// Logger instance
	private static final Logger log = LogManager.getLogger(HomePage.class);
	
	
	/*WebElements using FindBy locators*/
	@FindBy(xpath = "//h1[contains(text(), 'Understand your policy')]")
	private WebElement understandYourPolicyHeader;
	
	
	/*WebElements using String locators*/
	private String sectionXpath = "//p[normalize-space() = '#']";
	
	private String productXpath = "//span[normalize-space() = '#']/ancestor::div[contains(@class, 'mantine-Paper-root')]";
	
	
	// Constructor
	public HomePage(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
	}
	
	/*
	 * Method to scroll to Term or Health section on the Homepage.
	 * 
	 * @param sectionName - The name of the section to scroll to (e.g., "Term","Health").
	 */
	public void scrollToSection(String sectionName) 
	{
		By sectionElementLocator = By.xpath(sectionXpath.replace("#", sectionName));
		
		WebElement sectionElement = wait.until(ExpectedConditions.presenceOfElementLocated(sectionElementLocator));
		
		CommonUtilities.jsScroll(sectionElement, this.driver);
	}
	
	/*
	 * Method to select an insurance product by its name.
	 * 
	 * @param productName - The name of the insurance product to select.
	 */
	public void selectInsurance(String productName)
	{
		By productElementLocator = By.xpath(productXpath.replace("#", productName));
		
		WebElement productElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productElementLocator));
		
		productElement.click();
		
		log.info(productName + " clicked successfully");
		CommonUtilities.waitForElementToBePresent(driver, understandYourPolicyHeader);
		//System.out.println("Page title after clicking " + productName + ": " + driver.getTitle());
	}

	/*
	 * Method to verify if the user is on the expected page by checking the presence of a specific header.
	 * 
	 * @param expectedPage - The expected page identifier (not used in this implementation).
	 * 
	 * @return true if the header is displayed, false otherwise.
	 */
	public boolean verifyUnderstandPolicyPage() 
	{
		return CommonUtilities.verifyPageLoaded(driver, "Understand your policy");
	}
	
	
}
