package mobile.hooks;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mobile.base.SharedDriver;
import org.openqa.selenium.OutputType;

import java.util.Map;

public class Hooks {

    @Before
    public void setup(Scenario scenario) {
        try {
            // Get or create driver
            AppiumDriver driver = SharedDriver.getDriver();

            // Log scenario info
            Map<String, String> deviceDetails = SharedDriver.getAllDeviceDetails();
            String deviceName = deviceDetails.get("deviceName");

            System.out.println("Starting scenario: " + scenario.getName() +
                    " on device: " + deviceName);

        } catch (Exception e) {
            System.err.println("Error in setup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            // Take screenshot if scenario failed
            if (scenario.isFailed()) {
                System.out.println("Scenario failed: " + scenario.getName());

                // Take screenshot
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
            }

            // Get device details for logging
            Map<String, String> deviceDetails = SharedDriver.getAllDeviceDetails();
            String deviceInfo = String.format(
                    "Device: %s, Platform: %s %s, UDID: %s",
                    deviceDetails.get("deviceName"),
                    deviceDetails.get("platformName"),
                    deviceDetails.get("platformVersion"),
                    deviceDetails.get("udid")
            );

            System.out.println("Finished scenario: " + scenario.getName() +
                    " on " + deviceInfo);

            // Close driver
            SharedDriver.quitDriver();

        } catch (Exception e) {
            System.err.println("Error in tearDown: " + e.getMessage());
        }
    }
}