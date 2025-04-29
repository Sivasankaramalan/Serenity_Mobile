package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import mobile.base.SharedDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AppHelper {

    /**
     * Launch app using package name or bundle ID
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return true if successful, false otherwise
     */
    public static boolean launchApp(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).activateApp(appId);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).activateApp(appId);
                return true;
            } else {
                System.err.println("Unsupported driver type for activateApp");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to launch app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close app without terminating session
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return true if successful, false otherwise
     */
    public static boolean closeApp(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).terminateApp(appId);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).terminateApp(appId);
                return true;
            } else {
                System.err.println("Unsupported driver type for terminateApp");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to close app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if app is installed
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return true if installed, false otherwise
     */
    public static boolean isAppInstalled(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                return ((AndroidDriver) driver).isAppInstalled(appId);
            } else if (driver instanceof IOSDriver) {
                return ((IOSDriver) driver).isAppInstalled(appId);
            } else {
                System.err.println("Unsupported driver type for isAppInstalled");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to check if app is installed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Install app from local path
     * @param appPath Absolute path to the app file (.apk or .ipa)
     * @return true if successful, false otherwise
     */
    public static boolean installApp(String appPath) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).installApp(appPath);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).installApp(appPath);
                return true;
            } else {
                System.err.println("Unsupported driver type for installApp");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to install app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Uninstall app
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return true if successful, false otherwise
     */
    public static boolean uninstallApp(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).removeApp(appId);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).removeApp(appId);
                return true;
            } else {
                System.err.println("Unsupported driver type for removeApp");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to uninstall app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reset app (clear app data)
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return true if successful, false otherwise
     */
    public static boolean resetApp(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            // First terminate the app
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).terminateApp(appId);

                // For Android, clear app data
                Map<String, Object> args = new HashMap<>();
                args.put("command", "pm");
                args.put("args", new String[]{"clear", appId});
                ((AndroidDriver) driver).executeScript("mobile: shell", args);

                // Relaunch the app
                ((AndroidDriver) driver).activateApp(appId);
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).terminateApp(appId);
                ((IOSDriver) driver).activateApp(appId);
                return true;
            } else {
                System.err.println("Unsupported driver type for resetApp");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to reset app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get app state
     * @param appId Package name (Android) or bundle ID (iOS)
     * @return App state as string
     */
    public static String getAppState(String appId) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                boolean isRunning = ((AndroidDriver) driver).isAppInstalled(appId);
                return isRunning ? "INSTALLED" : "NOT_INSTALLED";
            } else if (driver instanceof IOSDriver) {
                io.appium.java_client.appmanagement.ApplicationState state =
                        ((IOSDriver) driver).queryAppState(appId);
                return state.toString();
            } else {
                return "UNKNOWN_DRIVER_TYPE";
            }
        } catch (Exception e) {
            System.err.println("Failed to get app state: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * Put app in background for a specified duration
     * @param durationSeconds Duration in seconds
     * @return true if successful, false otherwise
     */
    public static boolean backgroundApp(int durationSeconds) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(durationSeconds));
                return true;
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).runAppInBackground(Duration.ofSeconds(durationSeconds));
                return true;
            } else {
                System.err.println("Unsupported driver type for runAppInBackground");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to background app: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get current activity (Android only)
     * @return Current activity name or null if error
     */
    public static String getCurrentActivity() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                return ((AndroidDriver) driver).currentActivity();
            } else {
                System.err.println("Getting current activity is only supported on Android");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to get current activity: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get current package (Android only)
     * @return Current package name or null if error
     */
    public static String getCurrentPackage() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            if (driver instanceof AndroidDriver) {
                return ((AndroidDriver) driver).getCurrentPackage();
            } else {
                System.err.println("Getting current package is only supported on Android");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to get current package: " + e.getMessage());
            return null;
        }
    }
}