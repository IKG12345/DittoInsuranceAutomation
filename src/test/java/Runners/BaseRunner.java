package Runners;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions
(

    features = {"src/test/resources/features"},
   
    glue = {"StepDefinitions", "TestHooks"},
    
    plugin = {
        "pretty",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "timeline:test-output-thread/"
    },
    
    monochrome = true, 
    publish = true,
    tags = "@SC01"
)
public class BaseRunner extends AbstractTestNGCucumberTests 
{
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() 
    {
        return super.scenarios();
    }
}