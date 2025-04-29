package mobile.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import mobile.utils.DeviceConfigReader;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class MobileDriverSource implements DriverSource {

    private static final List<Map<String, String>> deviceConfigs;
    private static int currentDeviceIndex = 0;

    static {
        // Initialize device configurations
        try {
            deviceConfigs = DeviceConfigReader.readDeviceConfigs("src/test/resources/devices.json");
            if (deviceConfigs.isEmpty()) {
                throw new RuntimeException("No device configurations found in devices.json");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load device configurations", e);
        }
    }

    @Override
    public WebDriver newDriver() {
        try {
            // Distribute devices among threads in a round-robin fashion
            Map<String, String> deviceConfig;
            synchronized (MobileDriverSource.class) {
                int index = currentDeviceIndex % deviceConfigs.size();
                currentDeviceIndex++;
                deviceConfig = deviceConfigs.get(index);
            }

            return createDriverForDevice(deviceConfig);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create driver: " + e.getMessage(), e);
        }
    }

    private WebDriver createDriverForDevice(Map<String, String> deviceConfig) {
        try {
            String deviceName = deviceConfig.get("deviceName");
            String platformName = deviceConfig.get("platformName");
            String platformVersion = deviceConfig.get("platformVersion");
            String udid = deviceConfig.get("udid");
            String appiumPort = deviceConfig.get("appiumPort");

            System.out.println("Creating driver for device: " + deviceName +
                    " (UDID: " + udid + ") on port " + appiumPort);

            // Set up capabilities
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformName", platformName);
            capabilities.setCapability("platformVersion", platformVersion);

            // Set UDID if provided
            if (udid != null && !udid.isEmpty()) {
                capabilities.setCapability("udid", udid);
            }

            // Platform-specific capabilities
            if (platformName.equalsIgnoreCase("android")) {
                String appPackage = deviceConfig.get("appPackage");
                String appActivity = deviceConfig.get("appActivity");

                capabilities.setCapability("appPackage", appPackage);
                capabilities.setCapability("appActivity", appActivity);
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("noReset", false);
                capabilities.setCapability("fullReset", false);

                // Initialize Android driver
                URL appiumUrl = new URL("http://localhost:" + appiumPort + "/wd/hub");
                return new AndroidDriver(appiumUrl, capabilities);

            } else if (platformName.equalsIgnoreCase("ios")) {
                String bundleId = deviceConfig.get("bundleId");

                capabilities.setCapability("bundleId", bundleId);
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("autoAcceptAlerts", true);
                capabilities.setCapability("noReset", false);
                capabilities.setCapability("fullReset", false);

                // Initialize iOS driver
                URL appiumUrl = new URL("http://localhost:" + appiumPort + "/wd/hub");
                return new IOSDriver(appiumUrl, capabilities);

            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create driver for device: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}