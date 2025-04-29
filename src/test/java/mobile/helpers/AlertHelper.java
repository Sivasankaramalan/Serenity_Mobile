package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertHelper {

    private static final int DEFAULT_TIMEOUT = 10; // seconds

    /**
     * Check if alert is present
     * @return true if alert is present, false otherwise
     */
    public static boolean isAlertPresent() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking for alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Wait for alert to be present
     * @param timeoutSeconds Timeout in seconds
     * @return true if alert appears within timeout, false otherwise
     */
    public static boolean waitForAlert(int timeoutSeconds) {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            System.err.println("Alert did not appear within timeout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Accept alert (click OK/Yes/Accept)
     * @return true if successful, false otherwise
     */
    public static boolean acceptAlert() {
        try {
            if (!waitForAlert(DEFAULT_TIMEOUT)) {
                return false;
            }

            AppiumDriver driver = SharedDriver.getDriver();
            Alert alert = driver.switchTo().alert();
            alert.accept();
            return true;
        } catch (Exception e) {
            System.err.println("Failed to accept alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Dismiss alert (click Cancel/No/Dismiss)
     * @return true if successful, false otherwise
     */
    public static boolean dismissAlert() {
        try {
            if (!waitForAlert(DEFAULT_TIMEOUT)) {
                return false;
            }

            AppiumDriver driver = SharedDriver.getDriver();
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            return true;
        } catch (Exception e) {
            System.err.println("Failed to dismiss alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get alert text
     * @return Alert text or null if no alert present
     */
    public static String getAlertText() {
        try {
            if (!waitForAlert(DEFAULT_TIMEOUT)) {
                return null;
            }

            AppiumDriver driver = SharedDriver.getDriver();
            Alert alert = driver.switchTo().alert();
            return alert.getText();
        } catch (Exception e) {
            System.err.println("Failed to get alert text: " + e.getMessage());
            return null;
        }
    }

    /**
     * Enter text in alert prompt
     * @param text Text to enter
     * @return true if successful, false otherwise
     */
    public static boolean sendKeysToAlert(String text) {
        try {
            if (!waitForAlert(DEFAULT_TIMEOUT)) {
                return false;
            }

            AppiumDriver driver = SharedDriver.getDriver();
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(text);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send keys to alert: " + e.getMessage());
            return false;
        }
    }
}