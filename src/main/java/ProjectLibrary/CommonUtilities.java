package ProjectLibrary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class CommonUtilities 
{
	
	private static final Logger log = LogManager.getLogger(CommonUtilities.class);
	
	
	/*
	 * This is a utility method to scroll to a specific WebElement using JavaScriptExecutor.
	 * @param element - The WebElement to scroll to.
	 * @param driver - The WebDriver instance in the page class.
	 * */
	public static void jsScroll(WebElement element, WebDriver driver)
	{
		try
		{
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			log.info("Scrolled to "+element + " successfully");
		}
		catch(Exception e)
		{
			log.error("Unable to scroll to " + element + " - " + e.getMessage());
		}
	}
	
	/*
	 * This is a utility method to click on a WebElement using JavaScriptExecutor.
	 * 
	 * @param element - The WebElement to click.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 */
	public static void jsClick(WebElement element, WebDriver driver) 
	{
		try 
		{
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
			log.info("Clicked on " + element + " successfully using JS Executor");
		} 
		catch (Exception e) 
		{
			log.error("Unable to click on " + element + " using JS Executor - " + e.getMessage());
		}
	}
	
	/*
	 * This is a utility method to wait for the page to be fully loaded.
	 * This ensures that all JavaScript, images, and CSS are finished loading before interacting with any page elements.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 * 
	 * @param duration - The maximum duration to wait for the page to load (in
	 * seconds).
	 */
	public static void waitForPageToBeLoaded(WebDriver driver, int duration)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
			wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
			log.info("Page loaded successfully");
		}
		catch(Exception e)
		{
			log.error("Page did not load within " + duration + " seconds - " + e.getMessage());
		}
	}
	
	/*
	 * This is a utility method to wait for the page URL to change to the specified target URL. 
	 * This is useful after actions that trigger navigation to a new page.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 * 
	 * @param targetURL - The URL to wait to change from.
	 */
	public static void waitForPageToChange(WebDriver driver, String partialURL) 
	{
        try 
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(partialURL)));
            
            waitForPageToBeLoaded(driver, 10);
            
            log.info("Page changed successfully. New URL: " + driver.getCurrentUrl());
        } 
        catch (Exception e) 
        {
            log.error("Page did not change within timeout: " + e.getMessage());
        }
    }
	
	/*
	 * This is a utility method to pause the execution for a specific number of seconds.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 * 
	 * @param seconds - The number of seconds to wait.
	 */
	public static void waitForSpecificTime(WebDriver driver, int seconds) 
	{
        try 
        {
            Thread.sleep(seconds * 1000L);
        } 
        catch (InterruptedException e) 
        {
            log.error("Wait interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
	
	/*
	 * This is a utility method to wait for a WebElement to be visible and clickable before interaction.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 * 
	 * @param element - The WebElement to wait for.
	 */
	public static void waitForElementToBePresent(WebDriver driver, WebElement element) 
	{
        try 
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.presenceOfElementLocated(((org.openqa.selenium.By) element)));
        } 
        catch (Exception e) 
        {
            log.error("Element not ready for interaction: " + e.getMessage());
        }
    }
	
	/*
	 * This is a utility method to verify if the page has loaded by checking for a specific header text.
	 * 
	 * @param driver - The WebDriver instance in the page class.
	 * 
	 * @param expectedHeaderText - The expected header text to verify.
	 */
	public static boolean verifyPageLoaded(WebDriver driver, String expectedHeaderText)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String dynamicHeaderXpath = "//h1[contains(text(), '" + expectedHeaderText + "')]";
			WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicHeaderXpath)));
			log.info("Successfully landed in page with header: " + expectedHeaderText);
	        return header.isDisplayed();
		}
		catch (Exception e) 
		{
			log.error("Page verification failed: " + e.getMessage());
			return false;
		}
	}

}
