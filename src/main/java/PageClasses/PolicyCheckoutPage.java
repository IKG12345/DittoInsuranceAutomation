package PageClasses;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import ProjectLibrary.CommonUtilities;

public class PolicyCheckoutPage 
{
	private WebDriver driver;
	
	private WebDriverWait wait;
	
	private Logger log = LogManager.getLogger(PolicyCheckoutPage.class);
	
	/*WebElements using FindBy locators*/
	@FindBy(xpath = "//span[normalize-space() = 'Base Premium']/following-sibling::span[contains(text(), '₹')]")
	private WebElement basePremiunValue;
	
	@FindBy(xpath = "//span[normalize-space() = 'Total Premium']/following-sibling::span[contains(text(), '₹')]")
	private WebElement totalPremiumValue;
	
	/*@FindBy(xpath = "//input[contains(@class, 'mantine-Checkbox-input') and @type = 'checkbox']/ancestor::div[contains(@class, 'mantine-Checkbox-root')]/following-sibling::div/span")
	private List<WebElement> AddOnsProducts;
	
	@FindBy(xpath = "//input[contains(@class, 'mantine-Checkbox-input') and @type = 'checkbox']/ancestor::div[contains(@class, 'mantine-Checkbox-root')]/following-sibling::div/span/ancestor::div[contains(@class,'mantine-Grid-col')]/following-sibling::div/descendant::span")
	private List<WebElement> AddOnsPrices;
	
	@FindBy(xpath = "//input[contains(@class, 'mantine-Checkbox-input') and @type = 'checkbox']")
	private List<WebElement> AddOnsCheckboxes;*/
	
	
	/*WebElements using String locators*/
	private String addOnSubsectionValue = "//div[contains(@class,'_premiumSummaryStack')]/descendant::span[contains(text(), '#')]/following-sibling::span[contains(text(), '₹')]";
	
	/*
	 * Inner class to hold Add-On data.
	 */
	public static class AddOnData 
	{
        public String name;
        public double price;
        public boolean isSelected;

        public AddOnData(String name, double price, boolean isSelected) 
        {
            this.name = name;
            this.price = price;
            this.isSelected = isSelected;
        }
    }
	
	// Constructor
	public PolicyCheckoutPage(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
	}
	
	
	/* 
	 * Method to parse price text and convert it to a double value.
	 * 
	 * @param priceText - The price text to parse (e.g., "₹ 1,23,456").
	 */
    private double parsePrice(String priceText) 
    {
        if (priceText == null || priceText.isEmpty()) 
        	return 0.0;
        
        String clean = priceText.replaceAll("[^0-9.]", "");
        
        try 
        {
            return Double.parseDouble(clean);
        } 
        catch (NumberFormatException e) 
        {
            return 0.0;
        }
    }
    
	/*
	 * Method to get total value of add-ons under a specific section.
	 * 
	 * @param sectionName - The name of the section ("Recommended Add-ons" or "Other Add-ons").
	 * @return The total value of add-ons in the specified section.
	 */
    public double getSectionSubTotal(String sectionName)
    {
    	try
    	{
    		WebElement subSectionTotal = wait.until(driver -> driver.findElement(By.xpath(addOnSubsectionValue.replace("#", sectionName))));
    	
    		String subSectionTotalPrice = subSectionTotal.getText();
    	
    		return parsePrice(subSectionTotalPrice);
    	}
    	catch (Exception e) 
    	{
            log.warn("Could not find sub-total for section: " + sectionName + ". Assuming 0.0");
            return 0.0;
    	}
    }

	/*
	 * Method to verify if the policy premium calculation is correct.
	 * 
	 * @return true if the premium calculation is correct, false otherwise.
	 */
    public boolean verifyPolicyPremiumCalculation() 
    {
    	double basePremium = parsePrice(basePremiunValue.getText());; 
    	double expectedTotal = parsePrice(totalPremiumValue.getText());
    	
        log.info("Starting Premium Verification");

        double mandatoryTotal   = getSectionSubTotal("Mandatory Add-ons");
        double recommendedTotal = getSectionSubTotal("Recommended Add-ons");
        double otherTotal = getSectionSubTotal("Other Add-ons");

        double calculatedTotal = basePremium + mandatoryTotal + recommendedTotal + otherTotal;

        double roundedCalculated = Math.round(calculatedTotal * 100.0) / 100.0;
        double roundedExpected = Math.round(expectedTotal * 100.0) / 100.0;

        log.info("------------------------------------------------");
        log.info("Base Premium:   " + basePremium);
        log.info("Mandatory Total:   " + mandatoryTotal);
        log.info("Recommended Add-On Total:   " + recommendedTotal);
        log.info("Other Add-On Total:   " + otherTotal);
        log.info("------------------------------------------------");
        log.info("Calculated Sum:   " + roundedCalculated);
        log.info("UI Expected Total:   " + roundedExpected);
        log.info("------------------------------------------------");
        
        if (Math.abs(roundedCalculated - roundedExpected) > 0.05) 
        {
            log.error("Premium Mismatch! Expected: " + roundedExpected + ", Found: " + roundedCalculated);
            return false;
        }
        
        log.info("Premium Verification PASSED.");
        return true;
    }

}
