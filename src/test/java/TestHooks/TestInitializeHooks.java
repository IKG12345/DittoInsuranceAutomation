package TestHooks;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import ProjectLibrary.ConfigReader;
import ProjectLibrary.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class TestInitializeHooks 
{
	private DriverFactory driverFactory;
	private WebDriver driver;
	private ConfigReader configReader;
	Properties prop;
	
	private static final Logger log = LogManager.getLogger(TestInitializeHooks.class);
	
	private static ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();
	
	/* 
     * This method will be executed before the test execution starts.
     * It initializes the configuration properties of the test.
     * 
	 */
	@Before(order=0)
	public void getProperty()
	{
		configReader = new ConfigReader();
		prop = configReader.initializeProperty();
		log.info("Configuration properties loaded successfully");
	}
	
	/*
	 * This method will be executed before the test execution starts. 
	 * It initializes the browser specified in the configuration properties.
	 * 
	 */
	@Before(order=1)
	public void launchBrowser(Scenario scenario)
	{
		currentScenario.set(scenario);
		
		String browserName = prop.getProperty("browser");
		log.info("Starting scenario: " + scenario.getName());
		
		driverFactory = new DriverFactory();
		driver = driverFactory.initializeDriver(browserName);
	}
	
	/*
	 * This method will be executed after the test execution ends. 
	 * It quits the browser session.
	 * 
	 */
	@After(order=0)
	public void quitBrowser()
	{
		if(driver != null)
		{
			driver.quit();
			log.info("Browser session ended successfully");
		}
	}
	
	/*
	 * This method will be executed after the test execution ends. It captures a
	 * screenshot if the scenario fails and attaches it to the report.
	 * 
	 */
	@After(order=1)
	public void tearDown(Scenario scenario)
	{
		if(scenario.isFailed())
		{
			log.info("Scenario failed: " + scenario.getName());
			String screenshotName = scenario.getName().replaceAll(" ", "_");
			byte sourcePath[] = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(sourcePath, "image/png", screenshotName);
		}
		else
		{
			log.info("Scenario passed: " + scenario.getName());
		}
	}
	
	/*
	 * This method will be used in Step Definitions to report a step pass status with a message.
	 * 
	 * @param message - The message to be logged and attached to the report.
	 */
	public static void reportPass(String message) 
	{
        try 
        {
            log.info("PASS: " + message);
           
            currentScenario.get().log(message);
            
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            currentScenario.get().attach(screenshot, "image/png", "Pass_Screenshot");
            
        } 
        catch (Exception e) 
        {
            log.error("Error while reporting pass: " + e.getMessage());
        }
    }
	
	/*
	 * This method will be used in Step Definitions to report a step fail status with a message.
	 * 
	 * @param message - The message to be logged and attached to the report.
	 */
	public static void reportFail(String message) 
	{
        try 
        {
            log.error("FAIL: " + message);

            currentScenario.get().log(message);
 
            if(DriverFactory.getDriver() != null) 
            {
                byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
                currentScenario.get().attach(screenshot, "image/png", "Fail_Screenshot");
            }
            
        } 
        catch (Exception e) 
        {
            log.error("Error while reporting fail: " + e.getMessage());
        }
        
        Assert.fail(message);
	}
}
