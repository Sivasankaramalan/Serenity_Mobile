package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitHelper {

    private static final int DEFAULT_TIMEOUT = 15; // seconds

    /**
     * Wait for a condition with custom timeout
     * @param condition The condition to wait for
     * @param timeoutSeconds Timeout in seconds
     * @param <T> Return type of the condition
     * @return The result of the condition or null if timeout
     */
    public static <T> T waitFor(Function<AppiumDriver, T> condition, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(driver -> condition.apply((AppiumDriver) driver));
        } catch (Exception e) {
            System.err.println("Wait condition failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Wait for element to be visible
     * @param locator Locator of the element
     * @param timeoutSeconds Timeout in seconds
     * @return The visible element or null if timeout
     */
    public static WebElement waitForVisibility(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println("Element not visible within timeout: " + locator);
            return null;
        }
    }

    /**
     * Wait for element to be visible with default timeout
     * @param locator Locator of the element
     * @return The visible element or null if timeout
     */
    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable
     * @param locator Locator of the element
     * @param timeoutSeconds Timeout in seconds
     * @return The clickable element or null if timeout
     */
    public static WebElement waitForClickability(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            System.err.println("Element not clickable within timeout: " + locator);
            return null;
        }
    }

    /**
     * Wait for element to be clickable with default timeout
     * @param locator Locator of the element
     * @return The clickable element or null if timeout
     */
    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be invisible
     * @param locator Locator of the element
     * @param timeoutSeconds Timeout in seconds
     * @return true if element becomes invisible, false otherwise
     */
    public static boolean waitForInvisibility(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println("Element still visible after timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for text to be present in element
     * @param locator Locator of the element
     * @param text Text to wait for
     * @param timeoutSeconds Timeout in seconds
     * @return true if text is present, false otherwise
     */
    public static boolean waitForTextPresent(By locator, String text, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            System.err.println("Text '" + text + "' not present in element after timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for number of elements to be a certain count
     * @param locator Locator of the elements
     * @param count Expected count
     * @param timeoutSeconds Timeout in seconds
     * @return true if count matches, false otherwise
     */
    public static boolean waitForElementCount(By locator, int count, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            return wait.until(driver -> {
                List<WebElement> elements = driver.findElements(locator);
                return elements.size() == count;
            });
        } catch (Exception e) {
            System.err.println("Element count did not match " + count + " after timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for page to load completely
     * @param timeoutSeconds Timeout in seconds
     * @return true if page loaded, false otherwise
     */
    public static boolean waitForPageToLoad(int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));

            // For Android, we can check if the progress bar is gone
            if (SharedDriver.getDriver().getCapabilities().getPlatformName().toString().equalsIgnoreCase("android")) {
                // Wait for Android progress indicators to disappear
                By progressBar = By.xpath("//*[contains(@class, 'ProgressBar')]");
                return wait.until(ExpectedConditions.invisibilityOfElementLocated(progressBar));
            }
            // For iOS, we can check for activity indicators
            else {
                // Wait for iOS activity indicators to disappear
                By activityIndicator = By.xpath("//XCUIElementTypeActivityIndicator");
                return wait.until(ExpectedConditions.invisibilityOfElementLocated(activityIndicator));
            }
        } catch (Exception e) {
            System.err.println("Page did not load completely after timeout");
            return false;
        }
    }

    /**
     * Wait for a specific amount of time
     * @param milliseconds Time to wait in milliseconds
     */
    public static void staticWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }
}