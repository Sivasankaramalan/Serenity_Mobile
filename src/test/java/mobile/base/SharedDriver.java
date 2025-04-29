package mobile.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SharedDriver {
    private static final String DEVICE_DETAILS_KEY = "deviceDetails";
    private static final ThreadLocal<AppiumDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * Get the driver
     * @return The AppiumDriver instance
     */
    public static AppiumDriver getDriver() {
        // Get from ThreadLocal
        AppiumDriver driver = DRIVER_THREAD_LOCAL.get();

        // If not found, create a new one
        if (driver == null) {
            driver = createDriver();
            DRIVER_THREAD_LOCAL.set(driver);
        }

        return driver;
    }

    /**
     * Create a new driver instance
     * @return The AppiumDriver instance
     */
    private static AppiumDriver createDriver() {
        try {
            // Get device configuration from system properties
            String deviceName = System.getProperty("deviceName", "Android Emulator");
            String platformName = System.getProperty("platformName", "Android");
            String platformVersion = System.getProperty("platformVersion", "11");
            String udid = System.getProperty("udid", "emulator-5554");
            String appiumPort = System.getProperty("appiumPort", "4723");
            String appPackage = System.getProperty("appPackage", "com.example.android");
            String appActivity = System.getProperty("appActivity", "com.example.android.MainActivity");
            String bundleId = System.getProperty("bundleId", "com.example.ios");

            // Log device details
            System.out.println("Creating driver for device: " + deviceName +
                    " (UDID: " + udid + ") on port " + appiumPort);
            System.out.println("App details: " + appPackage + "/" + appActivity);

            // Store device details for reporting
            setDeviceDetail("deviceName", deviceName);
            setDeviceDetail("platformName", platformName);
            setDeviceDetail("platformVersion", platformVersion);
            setDeviceDetail("udid", udid);
            setDeviceDetail("appiumPort", appiumPort);
            setDeviceDetail("appPackage", appPackage);
            setDeviceDetail("appActivity", appActivity);

            // Set up capabilities
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformName", platformName);
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("udid", udid);

            // Add automationName capability - required by newer Appium versions
            if (platformName.equalsIgnoreCase("android")) {
                capabilities.setCapability("automationName", "UiAutomator2");
            } else if (platformName.equalsIgnoreCase("ios")) {
                capabilities.setCapability("automationName", "XCUITest");
            }

            // Platform-specific capabilities
            AppiumDriver driver;
            URL appiumUrl = new URL("http://localhost:" + appiumPort + "/wd/hub");

            if (platformName.equalsIgnoreCase("android")) {
                capabilities.setCapability("appPackage", appPackage);
                capabilities.setCapability("appActivity", appActivity);
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("noReset", false);
                capabilities.setCapability("fullReset", false);

                // Initialize Android driver
                System.out.println("Creating Android driver with capabilities: " + capabilities);
                driver = new AndroidDriver(appiumUrl, capabilities);

            } else if (platformName.equalsIgnoreCase("ios")) {
                capabilities.setCapability("bundleId", bundleId);
                capabilities.setCapability("autoAcceptAlerts", true);
                capabilities.setCapability("noReset", false);
                capabilities.setCapability("fullReset", false);

                // Initialize iOS driver
                System.out.println("Creating iOS driver with capabilities: " + capabilities);
                driver = new IOSDriver(appiumUrl, capabilities);

            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }

            System.out.println("Created new driver for device: " + deviceName +
                    " (UDID: " + udid + ") on port " + appiumPort);

            return driver;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create driver: " + e.getMessage(), e);
        }
    }

    /**
     * Set device details in Serenity session
     * @param key Detail key
     * @param value Detail value
     */
    public static void setDeviceDetail(String key, String value) {
        Map<String, String> deviceDetails = getDeviceDetailsMap();
        deviceDetails.put(key, value);
        Serenity.setSessionVariable(DEVICE_DETAILS_KEY).to(deviceDetails);
    }

    /**
     * Get device detail from Serenity session
     * @param key Detail key
     * @return Detail value
     */
    public static String getDeviceDetail(String key) {
        Map<String, String> deviceDetails = getDeviceDetailsMap();
        return deviceDetails.get(key);
    }

    /**
     * Get all device details from Serenity session
     * @return Map of device details
     */
    public static Map<String, String> getAllDeviceDetails() {
        return getDeviceDetailsMap();
    }

    private static Map<String, String> getDeviceDetailsMap() {
        if (!Serenity.hasASessionVariableCalled(DEVICE_DETAILS_KEY)) {
            Serenity.setSessionVariable(DEVICE_DETAILS_KEY).to(new HashMap<String, String>());
        }
        return Serenity.sessionVariableCalled(DEVICE_DETAILS_KEY);
    }

    /**
     * Quit the driver
     */
    public static void quitDriver() {
        AppiumDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}