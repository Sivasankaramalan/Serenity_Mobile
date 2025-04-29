package mobile.utils;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class MobileUtilities {

    // Capture screenshot with filename
    public static void captureScreenshot(String fileName) throws IOException {
        AppiumDriver driver = SharedDriver.getDriver();
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get("target/screenshots/" + fileName + ".png");

        // Create directory if it doesn't exist
        Files.createDirectories(destination.getParent());

        // Copy screenshot to destination
        Files.copy(screenshot.toPath(), destination);
        System.out.println("Screenshot saved to: " + destination.toAbsolutePath());
    }

    // Swipe up
    public static void swipeUp(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(swipe));
    }

    // Tap by coordinates
    public static void tapByCoordinates(AppiumDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(tap));
    }

    // Take screenshot with timestamp
    public static void takeScreenshot() {
        try {
            // Get the driver from SharedDriver
            AppiumDriver driver = SharedDriver.getDriver();

            File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destination = Paths.get("target/screenshots/screen_" + timestamp + ".png");

            // Create directory if it doesn't exist
            Files.createDirectories(destination.getParent());

            // Copy screenshot to destination
            Files.copy(screenshotFile.toPath(), destination);
            System.out.println("Screenshot saved to: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    // Long press on element
    public static void longPress(AppiumDriver driver, int x, int y, long durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);

        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), x, y));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(), x, y));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(longPress));
    }

    // Wait for a specific duration
    public static void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }

    public static void logPageSource() {
        try {
            AppiumDriver driver = SharedDriver.getDriver();
            String pageSource = driver.getPageSource();
            System.out.println("=== PAGE SOURCE ===");
            System.out.println(pageSource);
            System.out.println("===================");

            // Optionally save to file
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destination = Paths.get("target/pagesource/source_" + timestamp + ".xml");
            Files.createDirectories(destination.getParent());
            Files.writeString(destination, pageSource);
        } catch (Exception e) {
            System.err.println("Failed to log page source: " + e.getMessage());
        }
    }
}