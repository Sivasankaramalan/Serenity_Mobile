package mobile.helpers;

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

    private static final int DEFAULT_TIMEOUT = 10; // seconds

    /**
     * Wait for an element to be visible
     * @param element The element to wait for
     * @param timeoutSeconds Timeout in seconds
     * @return true if element becomes visible within timeout, false otherwise
     */
    public static boolean waitForVisibility(WebElement element, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for an element to be visible with default timeout
     * @param element The element to wait for
     * @return true if element becomes visible within timeout, false otherwise
     */
    public static boolean waitForVisibility(WebElement element) {
        return waitForVisibility(element, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for an element to be clickable
     * @param element The element to wait for
     * @param timeoutSeconds Timeout in seconds
     * @return true if element becomes clickable within timeout, false otherwise
     */
    public static boolean waitForClickability(WebElement element, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click on an element with validation
     * @param element The element to click
     * @param elementName Name of the element for reporting
     * @return true if click was successful, false otherwise
     */
    public static boolean click(WebElement element, String elementName) {
        try {
            if (!waitForClickability(element, DEFAULT_TIMEOUT)) {
                System.err.println("Element '" + elementName + "' not clickable within timeout");
                MobileUtilities.takeScreenshot();
                return false;
            }
            element.click();
            System.out.println("Clicked on element: " + elementName);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to click on element '" + elementName + "': " + e.getMessage());
            MobileUtilities.takeScreenshot();
            return false;
        }
    }

    /**
     * Enter text into an element with validation
     * @param element The element to enter text into
     * @param text The text to enter
     * @param elementName Name of the element for reporting
     * @return true if text entry was successful, false otherwise
     */
    public static boolean enterText(WebElement element, String text, String elementName) {
        try {
            if (!waitForVisibility(element, DEFAULT_TIMEOUT)) {
                System.err.println("Element '" + elementName + "' not visible within timeout");
                MobileUtilities.takeScreenshot();
                return false;
            }
            element.clear();
            element.sendKeys(text);
            System.out.println("Entered text '" + text + "' in element: " + elementName);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to enter text in element '" + elementName + "': " + e.getMessage());
            MobileUtilities.takeScreenshot();
            return false;
        }
    }

    /**
     * Clear text from an element with validation
     * @param element The element to clear
     * @param elementName Name of the element for reporting
     * @return true if clear was successful, false otherwise
     */
    public static boolean clearText(WebElement element, String elementName) {
        try {
            if (!waitForVisibility(element, DEFAULT_TIMEOUT)) {
                System.err.println("Element '" + elementName + "' not visible within timeout");
                MobileUtilities.takeScreenshot();
                return false;
            }
            element.clear();
            System.out.println("Cleared text from element: " + elementName);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to clear text from element '" + elementName + "': " + e.getMessage());
            MobileUtilities.takeScreenshot();
            return false;
        }
    }

    /**
     * Get text from an element with validation
     * @param element The element to get text from
     * @param elementName Name of the element for reporting
     * @return The element text or null if operation failed
     */
    public static String getText(WebElement element, String elementName) {
        try {
            if (!waitForVisibility(element, DEFAULT_TIMEOUT)) {
                System.err.println("Element '" + elementName + "' not visible within timeout");
                MobileUtilities.takeScreenshot();
                return null;
            }
            String text = element.getText();
            System.out.println("Got text '" + text + "' from element: " + elementName);
            return text;
        } catch (Exception e) {
            System.err.println("Failed to get text from element '" + elementName + "': " + e.getMessage());
            MobileUtilities.takeScreenshot();
            return null;
        }
    }

    /**
     * Check if an element is displayed
     * @param element The element to check
     * @param elementName Name of the element for reporting
     * @return true if element is displayed, false otherwise
     */
    public static boolean isDisplayed(WebElement element, String elementName) {
        try {
            boolean isDisplayed = element.isDisplayed();
            System.out.println("Element '" + elementName + "' is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            System.out.println("Element '" + elementName + "' is not displayed");
            return false;
        }
    }

    /**
     * Find element by locator with wait
     * @param locator The locator to find the element
     * @param timeoutSeconds Timeout in seconds
     * @return The found element or null if not found
     */
    public static WebElement findElement(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println("Element not found with locator: " + locator);
            return null;
        }
    }

    /**
     * Find element by locator with default timeout
     * @param locator The locator to find the element
     * @return The found element or null if not found
     */
    public static WebElement findElement(By locator) {
        return findElement(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Find elements by locator
     * @param locator The locator to find the elements
     * @return List of found elements (may be empty)
     */
    public static List<WebElement> findElements(By locator) {
        try {
            return SharedDriver.getDriver().findElements(locator);
        } catch (Exception e) {
            System.err.println("Error finding elements with locator: " + locator);
            return List.of(); // Return empty list
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
                ((io.appium.java_client.android.AndroidDriver) driver).findElement(
                        io.appium.java_client.AppiumBy.androidUIAutomator(uiAutomatorString));
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

    /**
     * Get attribute value from element
     * @param element The element to get attribute from
     * @param attributeName The attribute name
     * @param elementName Name of the element for reporting
     * @return The attribute value or null if operation failed
     */
    public static String getAttribute(WebElement element, String attributeName, String elementName) {
        try {
            if (!waitForVisibility(element, DEFAULT_TIMEOUT)) {
                System.err.println("Element '" + elementName + "' not visible within timeout");
                return null;
            }
            String value = element.getAttribute(attributeName);
            System.out.println("Got attribute '" + attributeName + "' with value '" + value + "' from element: " + elementName);
            return value;
        } catch (Exception e) {
            System.err.println("Failed to get attribute '" + attributeName + "' from element '" + elementName + "': " + e.getMessage());
            return null;
        }
    }
}