package mobile.steps;

import mobile.base.SharedDriver;
import net.thucydides.core.annotations.Step;
import io.appium.java_client.AppiumDriver;

public class BaseStep {

    @Step("Print driver details")
    public void printDriverDetails() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver != null) {
                System.out.println("Application launched");
                System.out.println("Driver Platform: " + driver.getCapabilities().getCapability("platformName"));
                System.out.println("Driver Device: " + driver.getCapabilities().getCapability("deviceName"));
                System.out.println("Driver App: " + driver.getCapabilities().getCapability("appPackage"));
            } else {
                System.err.println("Driver is null — check initialization.");
            }
        } catch (Exception e) {
            System.err.println("Error getting driver details: " + e.getMessage());
        }
    }

    public void launchApp(){
        // Add verification that app is running
        AppiumDriver driver = SharedDriver.getDriver();
        if (driver != null) {
            try {
                // For Android
                if (driver.getCapabilities().getPlatformName().toString().equalsIgnoreCase("android")) {
                    String currentPackage = ((io.appium.java_client.android.AndroidDriver) driver).getCurrentPackage();
                    System.out.println("Current app package: " + currentPackage);

                    // Verify we're in the correct app
                    String expectedPackage = "com.act.mobile.apps";
                    if (!expectedPackage.equals(currentPackage)) {
                        System.err.println("WARNING: App is not running the expected package!");
                        System.err.println("Expected: " + expectedPackage + ", Actual: " + currentPackage);

                        // Try to force launch the app
                        ((io.appium.java_client.android.AndroidDriver) driver).activateApp(expectedPackage);
                        System.out.println("Attempted to force launch the app");
                    }
                }
                // For iOS
                else if (driver.getCapabilities().getPlatformName().toString().equalsIgnoreCase("ios")) {
                    // The correct method name is queryAppState
                    String bundleId = System.getProperty("appium.bundleId", "com.act.mobile.apps");
                    io.appium.java_client.ios.IOSDriver iosDriver = (io.appium.java_client.ios.IOSDriver) driver;

                    // Check app state
                    io.appium.java_client.appmanagement.ApplicationState appState =
                            iosDriver.queryAppState(bundleId);
                    System.out.println("Current app state for " + bundleId + ": " + appState);
                }
            } catch (Exception e) {
                System.err.println("Error verifying app state: " + e.getMessage());
            }
        }
    }
}