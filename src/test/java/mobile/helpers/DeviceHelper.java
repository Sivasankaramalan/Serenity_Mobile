package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import mobile.base.SharedDriver;
import mobile.utils.MobileUtilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.html5.Location;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DeviceHelper {

    /**
     * Press back button (Android only)
     * @return true if successful, false otherwise
     */
    public static boolean pressBackButton() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                return true;
            } else {
                System.err.println("Back button press is only supported on Android");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to press back button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Press home button
     * @return true if successful, false otherwise
     */
    public static boolean pressHomeButton() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
                return true;
            } else if (driver instanceof IOSDriver) {
                Map<String, Object> params = new HashMap<>();
                params.put("name", "home");
                ((IOSDriver) driver).executeScript("mobile:pressButton", params);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to press home button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lock the device
     * @param durationSeconds Duration to lock the device for (iOS only)
     * @return true if successful, false otherwise
     */
    public static boolean lockDevice(int durationSeconds) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).lockDevice();
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).lockDevice(Duration.ofSeconds(durationSeconds));
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to lock device: " + e.getMessage());
            return false;
        }
    }

    /**
     * Unlock the device
     * @return true if successful, false otherwise
     */
    public static boolean unlockDevice() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).unlockDevice();
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).unlockDevice();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to unlock device: " + e.getMessage());
            return false;
        }
    }

    /**
     * Set device orientation
     * @param orientation The orientation to set (LANDSCAPE or PORTRAIT)
     * @return true if successful, false otherwise
     */
    public static boolean setOrientation(ScreenOrientation orientation) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).rotate(orientation);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).rotate(orientation);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to set orientation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get device orientation
     * @return The current device orientation or null if error
     */
    public static ScreenOrientation getOrientation() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                return ((AndroidDriver) driver).getOrientation();
            } else if (driver instanceof IOSDriver) {
                return ((IOSDriver) driver).getOrientation();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Failed to get orientation: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get device time
     * @return The device time as string or null if error
     */
    public static String getDeviceTime() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                return ((AndroidDriver) driver).getDeviceTime();
            } else if (driver instanceof IOSDriver) {
                return ((IOSDriver) driver).getDeviceTime();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Failed to get device time: " + e.getMessage());
            return null;
        }
    }

    /**
     * Open notifications (Android only)
     * @return true if successful, false otherwise
     */
    public static boolean openNotifications() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).openNotifications();
                return true;
            } else {
                System.err.println("Opening notifications is only supported on Android");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to open notifications: " + e.getMessage());
            return false;
        }
    }

    /**
     * Toggle airplane mode (Android only)
     * @param enable true to enable, false to disable
     * @return true if successful, false otherwise
     */
    public static boolean toggleAirplaneMode(boolean enable) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                ConnectionState connectionState = androidDriver.getConnection();

                if (enable) {
                    androidDriver.setConnection(new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK));
                } else {
                    // Enable all connections (WiFi, Data, Airplane Mode off)
                    androidDriver.setConnection(new ConnectionState(
                            ConnectionState.WIFI_MASK | ConnectionState.DATA_MASK));
                }
                return true;
            } else {
                System.err.println("Toggling airplane mode is only supported on Android");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to toggle airplane mode: " + e.getMessage());
            return false;
        }
    }

    /**
     * Take screenshot and save to file
     * @param fileName Name of the file to save
     * @return true if successful, false otherwise
     */
    public static boolean takeScreenshot(String fileName) {
        try {
            MobileUtilities.captureScreenshot(fileName);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Set device location (requires location permissions)
     * @param latitude Latitude
     * @param longitude Longitude
     * @param altitude Altitude
     * @return true if successful, false otherwise
     */
    public static boolean setLocation(double latitude, double longitude, double altitude) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).setLocation(new Location(latitude, longitude, altitude));
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).setLocation(new Location(latitude, longitude, altitude));
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to set location: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get device system time
     * @return System time as string or null if error
     */
    public static String getSystemTime() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                Map<String, Object> args = new HashMap<>();
                args.put("command", "date");
                args.put("args", new String[]{"+%Y-%m-%d %H:%M:%S"});
                String result = (String) ((AndroidDriver) driver).executeScript("mobile: shell", args);
                return result.trim();
            } else if (driver instanceof IOSDriver) {
                // For iOS, we can use JavaScript to get the time
                return (String) ((IOSDriver) driver).executeScript("return new Date().toString()");
            }
            return null;
        } catch (Exception e) {
            System.err.println("Failed to get system time: " + e.getMessage());
            return null;
        }
    }
}