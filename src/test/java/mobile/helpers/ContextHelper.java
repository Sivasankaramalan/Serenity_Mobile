package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import org.openqa.selenium.ContextAware;

import java.util.Set;

public class ContextHelper {

    /**
     * Get all available contexts
     * @return Set of context handles or empty set if error
     */
    public static Set<String> getContextHandles() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware) {
                return ((ContextAware) driver).getContextHandles();
            } else {
                System.err.println("Driver does not support context switching");
                return Set.of();
            }
        } catch (Exception e) {
            System.err.println("Failed to get context handles: " + e.getMessage());
            return Set.of();
        }
    }

    /**
     * Get current context
     * @return Current context handle or null if error
     */
    public static String getCurrentContext() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware) {
                return ((ContextAware) driver).getContext();
            } else {
                System.err.println("Driver does not support context switching");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to get current context: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switch to native context
     * @return true if successful, false otherwise
     */
    public static boolean switchToNativeContext() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware) {
                ((ContextAware) driver).context("NATIVE_APP");
                return true;
            } else {
                System.err.println("Driver does not support context switching");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to switch to native context: " + e.getMessage());
            return false;
        }
    }

    /**
     * Switch to web context
     * @return true if successful, false otherwise
     */
    public static boolean switchToWebContext() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware contextAwareDriver) {
                Set<String> contexts = contextAwareDriver.getContextHandles();

                // Find first web view context
                for (String context : contexts) {
                    if (context.contains("WEBVIEW")) {
                        contextAwareDriver.context(context);
                        return true;
                    }
                }

                System.err.println("No web context found");
            } else {
                System.err.println("Driver does not support context switching");
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to switch to web context: " + e.getMessage());
            return false;
        }
    }

    /**
     * Switch to specific context
     * @param contextName Context name to switch to
     * @return true if successful, false otherwise
     */
    public static boolean switchToContext(String contextName) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware) {
                ((ContextAware) driver).context(contextName);
                return true;
            } else {
                System.err.println("Driver does not support context switching");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to switch to context '" + contextName + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Print all available contexts
     */
    public static void printAvailableContexts() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            if (driver instanceof ContextAware contextAwareDriver) {
                Set<String> contexts = contextAwareDriver.getContextHandles();

                System.out.println("Available contexts:");
                for (String context : contexts) {
                    System.out.println("  - " + context);
                }

                System.out.println("Current context: " + contextAwareDriver.getContext());
            } else {
                System.err.println("Driver does not support context switching");
            }
        } catch (Exception e) {
            System.err.println("Failed to print available contexts: " + e.getMessage());
        }
    }
}