package mobile.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mobile.base.SharedDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.OutputType;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());

        // Ensure driver is initialized
        AppiumDriver driver = SharedDriver.getDriver();

        // Make sure app is running
        try {
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                String appPackage = System.getProperty("appium.appPackage", "com.act.mobile.apps");

                // Check if app is running
                String currentPackage = androidDriver.getCurrentPackage();
                if (!appPackage.equals(currentPackage)) {
                    System.out.println("App is not running. Launching app: " + appPackage);
                    androidDriver.activateApp(appPackage);
                }
            } else if (driver instanceof IOSDriver) {
                IOSDriver iosDriver = (IOSDriver) driver;
                String bundleId = System.getProperty("appium.bundleId", "com.act.mobile.apps");

                // Check app state
                io.appium.java_client.appmanagement.ApplicationState appState =
                        iosDriver.queryAppState(bundleId);

                // If app is not running or in background, activate it
                if (appState != io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND) {
                    System.out.println("App is not in foreground. Current state: " + appState + ". Launching app: " + bundleId);
                    iosDriver.activateApp(bundleId);
                }
            }

            // Wait for app to stabilize
            Thread.sleep(3000);
        } catch (Exception e) {
            System.err.println("Error ensuring app is running: " + e.getMessage());
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("Completed scenario: " + scenario.getName() + " with status: " + scenario.getStatus());

        // Take screenshot if scenario failed
        if (scenario.isFailed()) {
            try {
                AppiumDriver driver = SharedDriver.getDriver();
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed scenario screenshot");
            } catch (Exception e) {
                System.err.println("Could not take failure screenshot: " + e.getMessage());
            }
        }
    }

    @After(order = 100)
    public void afterAll() {
        // Only quit driver after all scenarios
        SharedDriver.quitDriver();
    }
}