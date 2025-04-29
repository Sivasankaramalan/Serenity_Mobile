package mobile.helpers;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import mobile.utils.MobileUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ElementHelper {

    private static final int DEFAULT_TIMEOUT = 15; // seconds

    /**
     * Find element with timeout
     * @param by Locator
     * @param timeoutSeconds Timeout in seconds
     * @return WebElement or null if not found
     */
    public static WebElement findElement(By by, int timeoutSeconds) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            System.err.println("Element not found: " + by);
            return null;
        }
    }

    /**
     * Find element with default timeout
     * @param by Locator
     * @return WebElement or null if not found
     */
    public static WebElement findElement(By by) {
        return findElement(by, DEFAULT_TIMEOUT);
    }

    /**
     * Find elements with timeout
     * @param by Locator
     * @param timeoutSeconds Timeout in seconds
     * @return List of WebElements
     */
    public static List<WebElement> findElements(By by, int timeoutSeconds) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return driver.findElements(by);
        } catch (Exception e) {
            System.err.println("Elements not found: " + by);
            return List.of();
        }
    }

    /**
     * Find elements with default timeout
     * @param by Locator
     * @return List of WebElements
     */
    public static List<WebElement> findElements(By by) {
        return findElements(by, DEFAULT_TIMEOUT);
    }

    /**
     * Click on element
     * @param by Locator
     * @return true if successful, false otherwise
     */
    public static boolean click(By by) {
        try {
            WebElement element = findElement(by);
            if (element != null) {
                element.click();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to click element: " + by);
            return false;
        }
    }

    /**
     * Enter text into element
     * @param by Locator
     * @param text Text to enter
     * @return true if successful, false otherwise
     */
    public static boolean sendKeys(By by, String text) {
        try {
            WebElement element = findElement(by);
            if (element != null) {
                element.clear();
                element.sendKeys(text);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to send keys to element: " + by);
            return false;
        }
    }

    /**
     * Get text from element
     * @param by Locator
     * @return Element text or empty string if not found
     */
    public static String getText(By by) {
        try {
            WebElement element = findElement(by);
            return element != null ? element.getText() : "";
        } catch (Exception e) {
            System.err.println("Failed to get text from element: " + by);
            return "";
        }
    }

    /**
     * Check if element is displayed
     * @param by Locator
     * @return true if displayed, false otherwise
     */
    public static boolean isDisplayed(By by) {
        try {
            WebElement element = findElement(by, 5); // shorter timeout for checks
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @param by Locator
     * @return true if enabled, false otherwise
     */
    public static boolean isEnabled(By by) {
        try {
            WebElement element = findElement(by);
            return element != null && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is selected
     * @param by Locator
     * @return true if selected, false otherwise
     */
    public static boolean isSelected(By by) {
        try {
            WebElement element = findElement(by);
            return element != null && element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get attribute value from element
     * @param by Locator
     * @param attribute Attribute name
     * @return Attribute value or empty string if not found
     */
    public static String getAttribute(By by, String attribute) {
        try {
            WebElement element = findElement(by);
            return element != null ? element.getAttribute(attribute) : "";
        } catch (Exception e) {
            System.err.println("Failed to get attribute from element: " + by);
            return "";
        }
    }

    /**
     * Scroll to element by text
     * @param text The text to scroll to
     * @return true if scroll was successful, false otherwise
     */
    public static boolean scrollToElementByText(String text) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();

            // For Android
            if (driver.getCapabilities().getPlatformName().toString().equalsIgnoreCase("android")) {
                // Use the correct method for Android UIAutomator
                String uiAutomatorString = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"" + text + "\"))";
                driver.findElement(AppiumBy.androidUIAutomator(uiAutomatorString));
                return true;
            }
            // For iOS
            else {
                // iOS scrolling is more complex and may require a different approach
                // This is a simplified version
                int maxSwipes = 10;
                for (int i = 0; i < maxSwipes; i++) {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@label, '" + text + "') or contains(@value, '" + text + "')]"));
                    if (!elements.isEmpty()) {
                        return true;
                    }
                    MobileUtilities.swipeUp(driver);
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to scroll to element with text '" + text + "': " + e.getMessage());
            return false;
        }
    }
}