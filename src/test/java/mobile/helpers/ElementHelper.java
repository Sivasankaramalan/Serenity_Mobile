package mobile.helpers;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import mobile.base.SharedDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ElementHelper {
    private static final int DEFAULT_TIMEOUT_SECONDS = 15;
    private static final int DEFAULT_POLLING_INTERVAL_MS = 500;

    /**
     * Get the AppiumDriver instance from SharedDriver
     * @return AppiumDriver instance
     */
    private static AppiumDriver getDriver() {
        return SharedDriver.getDriver();
    }

    /**
     * Create a FluentWait instance with default settings
     * @return Wait<WebDriver> instance
     */
    private static FluentWait<AppiumDriver> createWait() {
        return createWait(DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Create a FluentWait instance with custom timeout
     * @param timeoutSeconds Timeout in seconds
     * @return Wait<WebDriver> instance
     */
    private static FluentWait<AppiumDriver> createWait(int timeoutSeconds) {
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL_MS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Find element by ID
     * @param id Element ID
     * @return WebElement
     */
    public static WebElement findElementById(String id) {
        try {
            return createWait().until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id(id)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element with ID '" + id + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Find element by accessibility ID
     * @param accessibilityId Accessibility ID
     * @return WebElement
     */
    public static WebElement findElementByAccessibilityId(String accessibilityId) {
        try {
            return createWait().until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId(accessibilityId)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element with accessibility ID '" + accessibilityId + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Find element by XPath
     * @param xpath XPath expression
     * @return WebElement
     */
    public static WebElement findElementByXPath(String xpath) {
        try {
            return createWait().until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath(xpath)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element with XPath '" + xpath + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Find elements by XPath
     * @param xpath XPath expression
     * @return List of WebElements
     */
    public static List<WebElement> findElementsByXPath(String xpath) {
        try {
            return createWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath(xpath)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Elements with XPath '" + xpath + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Find element by class name
     * @param className Class name
     * @return WebElement
     */
    public static WebElement findElementByClassName(String className) {
        try {
            return createWait().until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className(className)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element with class name '" + className + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Find elements by class name
     * @param className Class name
     * @return List of WebElements
     */
    public static List<WebElement> findElementsByClassName(String className) {
        try {
            return createWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.className(className)));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Elements with class name '" + className + "' not found after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Wait for element to be clickable
     * @param element WebElement
     * @return WebElement that is clickable
     */
    public static WebElement waitForElementToBeClickable(WebElement element) {
        try {
            return createWait().until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new ElementNotInteractableException("Element not clickable after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Wait for element to be visible
     * @param element WebElement
     * @return WebElement that is visible
     */
    public static WebElement waitForElementToBeVisible(WebElement element) {
        try {
            return createWait().until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new ElementNotVisibleException("Element not visible after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Wait for element to be invisible
     * @param element WebElement
     * @return true if element is invisible
     */
    public static boolean waitForElementToBeInvisible(WebElement element) {
        try {
            return createWait().until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element still visible after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
    }

    /**
     * Click on element with retry
     * @param element WebElement to click
     */
    public static void clickWithRetry(WebElement element) {
        int maxRetries = 3;
        int retries = 0;
        boolean clicked = false;

        while (!clicked && retries < maxRetries) {
            try {
                waitForElementToBeClickable(element).click();
                clicked = true;
            } catch (Exception e) {
                retries++;
                if (retries >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(1000); // Wait before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Send keys to element with retry
     * @param element WebElement to send keys to
     * @param text Text to send
     */
    public static void sendKeysWithRetry(WebElement element, String text) {
        int maxRetries = 3;
        int retries = 0;
        boolean sent = false;

        while (!sent && retries < maxRetries) {
            try {
                waitForElementToBeClickable(element).clear();
                element.sendKeys(text);
                sent = true;
            } catch (Exception e) {
                retries++;
                if (retries >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(1000); // Wait before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Scroll down until element is found or max scrolls reached
     * @param locator By locator to find element
     * @param maxScrolls Maximum number of scroll attempts
     * @return WebElement if found, null otherwise
     */
    public static WebElement scrollToElement(By locator, int maxScrolls) {
        for (int i = 0; i < maxScrolls; i++) {
            try {
                WebElement element = getDriver().findElement(locator);
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (NoSuchElementException e) {
                // Element not found, scroll down
                scrollDown();
            }
        }
        throw new NoSuchElementException("Element not found after scrolling " + maxScrolls + " times");
    }

    /**
     * Scroll down
     */
    public static void scrollDown() {
        AppiumDriver driver = getDriver();
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        int centerX = size.width / 2;

        try {
            driver.executeScript("mobile: scrollGesture",
                    Map.of(
                            "left", centerX - 100,
                            "top", startY,
                            "width", 200,
                            "height", startY - endY,
                            "direction", "down",
                            "percent", 1.0
                    )
            );
        } catch (Exception e) {
            // Fall back to TouchAction for older Appium versions
            new io.appium.java_client.TouchAction<>((PerformsTouchActions) driver)
                    .press(io.appium.java_client.touch.offset.PointOption.point(centerX, startY))
                    .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(Duration.ofMillis(300)))
                    .moveTo(io.appium.java_client.touch.offset.PointOption.point(centerX, endY))
                    .release()
                    .perform();
        }
    }

    /**
     * Scroll up
     */
    public static void scrollUp() {
        AppiumDriver driver = getDriver();
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);
        int centerX = size.width / 2;

        try {
            driver.executeScript("mobile: scrollGesture",
                    Map.of(
                            "left", centerX - 100,
                            "top", endY - startY,
                            "width", 200,
                            "height", endY - startY,
                            "direction", "up",
                            "percent", 1.0
                    )
            );
        } catch (Exception e) {
            // Fall back to TouchAction for older Appium versions
            new io.appium.java_client.TouchAction<>((PerformsTouchActions) driver)
                    .press(io.appium.java_client.touch.offset.PointOption.point(centerX, startY))
                    .waitAction(io.appium.java_client.touch.WaitOptions.waitOptions(Duration.ofMillis(300)))
                    .moveTo(io.appium.java_client.touch.offset.PointOption.point(centerX, endY))
                    .release()
                    .perform();
        }
    }

    /**
     * Take screenshot
     * @return Screenshot as byte array
     */
    public static byte[] takeScreenshot() {
        return getDriver().getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Check if element exists
     * @param locator By locator to find element
     * @param timeoutSeconds Timeout in seconds
     * @return true if element exists, false otherwise
     */
    public static boolean elementExists(By locator, int timeoutSeconds) {
        try {
            createWait(timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Check if element is displayed
     * @param locator By locator to find element
     * @param timeoutSeconds Timeout in seconds
     * @return true if element is displayed, false otherwise
     */
    public static boolean elementIsDisplayed(By locator, int timeoutSeconds) {
        try {
            return createWait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Wait for text to be present in element
     * @param element WebElement
     * @param text Text to wait for
     * @return true if text is present, false otherwise
     */
    public static boolean waitForTextToBePresentInElement(WebElement element, String text) {
        try {
            return createWait().until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Custom ElementNotVisibleException since it's deprecated in newer Selenium versions
     */
    public static class ElementNotVisibleException extends RuntimeException {
        public ElementNotVisibleException(String message) {
            super(message);
        }
    }
}