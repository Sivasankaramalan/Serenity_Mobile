package mobile.steps;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import mobile.base.SharedDriver;
import net.thucydides.core.annotations.Step;

public class BaseStep {

    @Step("Launch application")
    public void launchApp() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            // Ensure app is in foreground
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                String appPackage = SharedDriver.getDeviceDetail("appPackage");
                if (appPackage == null || appPackage.isEmpty()) {
                    appPackage = System.getProperty("appPackage", "com.example.android");
                }

                // Check if app is running
                if (!androidDriver.isAppInstalled(appPackage)) {
                    System.out.println("App is not installed: " + appPackage);
                } else {
                    // Start app if not already running
                    androidDriver.activateApp(appPackage);
                    System.out.println("Activated app: " + appPackage);
                }
            } else if (driver instanceof IOSDriver) {
                IOSDriver iosDriver = (IOSDriver) driver;
                String bundleId = SharedDriver.getDeviceDetail("bundleId");
                if (bundleId == null || bundleId.isEmpty()) {
                    bundleId = System.getProperty("bundleId", "com.example.ios");
                }

                // Check app state
                io.appium.java_client.appmanagement.ApplicationState appState =
                        iosDriver.queryAppState(bundleId);

                // If app is not running or in background, activate it
                if (appState != io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND) {
                    System.out.println("App is not in foreground. Current state: " + appState +
                            ". Launching app: " + bundleId);
                    iosDriver.activateApp(bundleId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to launch app: " + e.getMessage(), e);
        }
    }
}