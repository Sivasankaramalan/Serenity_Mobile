package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import mobile.utils.MobileUtilities;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AssertionHelper {

    /**
     * Assert that an element is displayed
     * @param element The element to check
     * @param elementName Name of the element for reporting
     */
    public static void assertElementDisplayed(WebElement element, String elementName) {
        try {
            Assert.assertTrue("Element '" + elementName + "' should be displayed", element.isDisplayed());
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Element '" + elementName + "' not displayed: " + e.getMessage());
        }
    }

    /**
     * Assert that an element is displayed with timeout
     * @param element The element to check
     * @param elementName Name of the element for reporting
     * @param timeoutSeconds Timeout in seconds
     */
    public static void assertElementDisplayed(WebElement element, String elementName, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(SharedDriver.getDriver(), Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Element '" + elementName + "' not displayed within " + timeoutSeconds + " seconds: " + e.getMessage());
        }
    }

    /**
     * Assert that an element is not displayed
     * @param element The element to check
     * @param elementName Name of the element for reporting
     */
    public static void assertElementNotDisplayed(WebElement element, String elementName) {
        try {
            Assert.assertFalse("Element '" + elementName + "' should not be displayed", element.isDisplayed());
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Element not found, which is what we want
            return;
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking if element '" + elementName + "' is not displayed: " + e.getMessage());
        }
    }

    /**
     * Assert that an element contains specific text
     * @param element The element to check
     * @param expectedText The expected text
     * @param elementName Name of the element for reporting
     */
    public static void assertElementContainsText(WebElement element, String expectedText, String elementName) {
        try {
            String actualText = element.getText();
            Assert.assertTrue("Element '" + elementName + "' should contain text '" + expectedText + "' but found '" + actualText + "'",
                    actualText.contains(expectedText));
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking text in element '" + elementName + "': " + e.getMessage());
        }
    }

    /**
     * Assert that an element has exact text
     * @param element The element to check
     * @param expectedText The expected text
     * @param elementName Name of the element for reporting
     */
    public static void assertElementHasExactText(WebElement element, String expectedText, String elementName) {
        try {
            String actualText = element.getText();
            Assert.assertEquals("Element '" + elementName + "' should have text '" + expectedText + "' but found '" + actualText + "'",
                    expectedText, actualText);
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking text in element '" + elementName + "': " + e.getMessage());
        }
    }

    /**
     * Assert that an element is enabled
     * @param element The element to check
     * @param elementName Name of the element for reporting
     */
    public static void assertElementEnabled(WebElement element, String elementName) {
        try {
            Assert.assertTrue("Element '" + elementName + "' should be enabled", element.isEnabled());
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking if element '" + elementName + "' is enabled: " + e.getMessage());
        }
    }

    /**
     * Assert that an element is disabled
     * @param element The element to check
     * @param elementName Name of the element for reporting
     */
    public static void assertElementDisabled(WebElement element, String elementName) {
        try {
            Assert.assertFalse("Element '" + elementName + "' should be disabled", element.isEnabled());
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking if element '" + elementName + "' is disabled: " + e.getMessage());
        }
    }

    /**
     * Assert that a list contains a specific number of elements
     * @param elements The list of elements
     * @param expectedCount The expected count
     * @param listName Name of the list for reporting
     */
    public static void assertElementCount(List<WebElement> elements, int expectedCount, String listName) {
        try {
            int actualCount = elements.size();
            Assert.assertEquals("List '" + listName + "' should contain " + expectedCount + " elements but found " + actualCount,
                    expectedCount, actualCount);
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking count of elements in list '" + listName + "': " + e.getMessage());
        }
    }

    /**
     * Assert that current screen contains a specific element by locator
     * @param locator The locator to find the element
     * @param elementName Name of the element for reporting
     */
    public static void assertScreenContainsElement(By locator, String elementName) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            List<WebElement> elements = driver.findElements(locator);
            Assert.assertTrue("Screen should contain element '" + elementName + "'", elements.size() > 0);
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            MobileUtilities.logPageSource();
            Assert.fail("Error checking if screen contains element '" + elementName + "': " + e.getMessage());
        }
    }

    /**
     * Assert that an element has a specific attribute value
     * @param element The element to check
     * @param attributeName The attribute name
     * @param expectedValue The expected attribute value
     * @param elementName Name of the element for reporting
     */
    public static void assertElementAttributeValue(WebElement element, String attributeName,
                                                   String expectedValue, String elementName) {
        try {
            String actualValue = element.getAttribute(attributeName);
            Assert.assertEquals("Element '" + elementName + "' should have attribute '" + attributeName +
                            "' with value '" + expectedValue + "' but found '" + actualValue + "'",
                    expectedValue, actualValue);
        } catch (Exception e) {
            MobileUtilities.takeScreenshot();
            Assert.fail("Error checking attribute '" + attributeName + "' of element '" + elementName + "': " + e.getMessage());
        }
    }
}