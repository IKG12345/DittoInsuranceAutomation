package ProjectLibrary;


import org.apache.logging.log4j.Logger;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory 
{
	// Initialize ThreadLocal variable to hold WebDriver instances for parallel execution
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	
	// Initialize Logger instance for logging
	public static final Logger log = LogManager.getLogger(DriverFactory.class);
	
	
	/*
	 * initializeDriver method initializes the browser according to the browser in which the test is to be executed.
	 * @param browser
	 * @return WebDriver
	 * */
	public WebDriver initializeDriver(String browser)
	{
		log.info("Initializing driver for browser: " + browser);
		
		// Set up WebDriver based on the specified browser
		if(browser.equalsIgnoreCase("chrome"))
		{
			WebDriverManager.chromedriver().setup();
			//Initialize ChromeOptions instance to add arguments
			ChromeOptions options = new ChromeOptions();
			options.addArguments("remote-allow-origins=*");
			//Create ChromeDriver instance with options
			tlDriver.set(new ChromeDriver(options));
		}
		else if(browser.equalsIgnoreCase("firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			tlDriver.set(new FirefoxDriver());
		}
		else if(browser.equalsIgnoreCase("edge"))
        {
            WebDriverManager.edgedriver().setup();
            tlDriver.set(new EdgeDriver());
        }
		else if(browser.equalsIgnoreCase("safari"))
		{
			WebDriverManager.safaridriver().setup();
			tlDriver.set(new SafariDriver());
		}
        else
        {
            log.error("Unsupported browser: " + browser);
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
		
		// Common WebDriver configurations
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		return getDriver();
	}
	
	/*
	 * getDriver method returns the WebDriver instance for the current thread.
	 * 
	 * @return WebDriver
	 */
	public static synchronized WebDriver getDriver()
	{
		// Return the WebDriver instance from ThreadLocal variable
		return tlDriver.get();
	}

}
