package mobile.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

import java.net.URL;
import java.time.Duration;

public class SharedDriver {
    private static AppiumDriver driver;
    private static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public static synchronized AppiumDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    private static void initializeDriver() {
        try {
            String platformName = getProperty("appium.platformName");
            String deviceName = getProperty("appium.deviceName");
            String automationName = getProperty("appium.automationName");
            String hubUrl = getProperty("appium.hub");

            if ("Android".equalsIgnoreCase(platformName)) {
                UiAutomator2Options options = new UiAutomator2Options()
                        .setDeviceName(deviceName)
                        .setAutomationName(automationName)
                        .setAppPackage(getProperty("appium.appPackage"))
                        .setAppActivity(getProperty("appium.appActivity"))
                        .setNoReset(Boolean.parseBoolean(getProperty("appium.noReset", "true")))
                        .setNewCommandTimeout(Duration.ofSeconds(300))
                        .setAutoGrantPermissions(true);

                // Check if we have an app path to install
                String appPath = getProperty("appium.app", "");
                if (!appPath.isEmpty()) {
                    options.setApp(appPath);
                }

                driver = new AndroidDriver(new URL(hubUrl), options);

                // Force start the app if it's not already running
                String appPackage = getProperty("appium.appPackage");
                String appActivity = getProperty("appium.appActivity");
                ((AndroidDriver) driver).activateApp(appPackage);

            } else if ("iOS".equalsIgnoreCase(platformName)) {
                XCUITestOptions options = new XCUITestOptions()
                        .setDeviceName(deviceName)
                        .setAutomationName(automationName)
                        .setBundleId(getProperty("appium.bundleId"))
                        .setNoReset(Boolean.parseBoolean(getProperty("appium.noReset", "true")))
                        .setNewCommandTimeout(Duration.ofSeconds(300))
                        .setAutoAcceptAlerts(true);

                // Check if we have an app path to install
                String appPath = getProperty("appium.app", "");
                if (!appPath.isEmpty()) {
                    options.setApp(appPath);
                }

                driver = new IOSDriver(new URL(hubUrl), options);

                // Force launch the app
                String bundleId = getProperty("appium.bundleId");
                ((IOSDriver) driver).activateApp(bundleId);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }

            System.out.println("Driver initialized for platform: " + platformName);
            System.out.println("App should now be launched");

            // Add a small delay to ensure app is fully launched
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static String getProperty(String key) {
        return EnvironmentSpecificConfiguration.from(environmentVariables).getProperty(key);
    }

    private static String getProperty(String key, String defaultValue) {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
                .getOptionalProperty(key)
                .orElse(defaultValue);
    }
}