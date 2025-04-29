package mobile.utils;


public class EnvironmentUtils {

    private static final String EMPTY = null;

    public EnvironmentUtils() {
        String environment = System.getProperty("environment").toLowerCase();
        String platform = System.getProperty("platform").toLowerCase();

    }

    public String getEnvironment() {
        return System.getProperty("environment");
    }


    public String getAppId() {

        if (isAndroid()) {
            return System.getProperty("appium.appPackage").toLowerCase();

        } else {
            return System.getProperty("appium.bundleId").toLowerCase();
        }
    }


    public String getAndroidAppName() {
        return System.getProperty("appium.appName").toLowerCase();

    }

    public String getPlatformName() {
        return System.getProperty("appium.platform").toLowerCase();
    }

    public boolean isAndroid() {
        return getPlatformName().equalsIgnoreCase("android");
    }

    public boolean isIOS() {
        return getPlatformName().equalsIgnoreCase("ios");
    }

    public void printDriverDetails() {
        System.out.println("Platform: " + getPlatformName());
        System.out.println("Environment: " + getEnvironment());
        System.out.println("App ID: " + getAppId());
        System.out.println("App Name: " + getAndroidAppName());

    }


}


