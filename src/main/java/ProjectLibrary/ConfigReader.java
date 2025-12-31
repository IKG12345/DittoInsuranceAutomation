package ProjectLibrary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader 
{
	private Properties prop;
	
	/*
	 * This method initializes the configuration properties by loading them from the
	 * config.properties file.
	 * 
	 * @return Properties object containing configuration key-value pairs.
	 */
	public Properties initializeProperty()
	{
		prop = new Properties();
		
		try
		{
			FileInputStream ip = new FileInputStream("src/test/resources/config.properties");
			prop.load(ip);
			
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
        {
            e.printStackTrace();
        }
		return prop;
	}
}
