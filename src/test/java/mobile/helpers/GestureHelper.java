package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import mobile.base.SharedDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public class GestureHelper {

    /**
     * Swipe from one point to another
     * @param startX Starting X coordinate
     * @param startY Starting Y coordinate
     * @param endX Ending X coordinate
     * @param endY Ending Y coordinate
     * @param durationMs Duration of swipe in milliseconds
     */
    public static void swipe(int startX, int startY, int endX, int endY, long durationMs) {
        AppiumDriver driver = SharedDriver.getDriver();

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(new Pause(finger, Duration.ofMillis(durationMs)));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    /**
     * Swipe up on the screen
     * @param percentOfScreen Percentage of screen height to swipe
     */
    public static void swipeUp(double percentOfScreen) {
        AppiumDriver driver = SharedDriver.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * (0.8 - percentOfScreen));

        swipe(startX, startY, startX, endY, 500);
    }

    /**
     * Swipe down on the screen
     * @param percentOfScreen Percentage of screen height to swipe
     */
    public static void swipeDown(double percentOfScreen) {
        AppiumDriver driver = SharedDriver.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * (0.2 + percentOfScreen));

        swipe(startX, startY, startX, endY, 500);
    }

    /**
     * Swipe left on the screen
     * @param percentOfScreen Percentage of screen width to swipe
     */
    public static void swipeLeft(double percentOfScreen) {
        AppiumDriver driver = SharedDriver.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startY = size.height / 2;
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * (0.8 - percentOfScreen));

        swipe(startX, startY, endX, startY, 500);
    }

    /**
     * Swipe right on the screen
     * @param percentOfScreen Percentage of screen width to swipe
     */
    public static void swipeRight(double percentOfScreen) {
        AppiumDriver driver = SharedDriver.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startY = size.height / 2;
        int startX = (int) (size.width * 0.2);
        int endX = (int) (size.width * (0.2 + percentOfScreen));

        swipe(startX, startY, endX, startY, 500);
    }

    /**
     * Tap at specific coordinates
     * @param x X coordinate
     * @param y Y coordinate
     */
    public static void tapAt(int x, int y) {
        AppiumDriver driver = SharedDriver.getDriver();

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(200)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    /**
     * Long press on an element
     * @param element Element to long press on
     * @param durationMs Duration of long press in milliseconds
     */
    public static void longPressOnElement(WebElement element, long durationMs) {
        AppiumDriver driver = SharedDriver.getDriver();

        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);

        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(new Pause(finger, Duration.ofMillis(durationMs)));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPress));
    }

    /**
     * Pinch to zoom out
     * @param element Element to pinch on
     */
    public static void pinchOut(WebElement element) {
        AppiumDriver driver = SharedDriver.getDriver();

        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence sequence1 = new Sequence(finger1, 1);
        Sequence sequence2 = new Sequence(finger2, 1);

        // First finger moves from center to top left
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), centerX, centerY));
        sequence1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX - 100, centerY - 100));
        sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Second finger moves from center to bottom right
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), centerX, centerY));
        sequence2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX + 100, centerY + 100));
        sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence1, sequence2));
    }

    /**
     * Pinch to zoom in
     * @param element Element to pinch on
     */
    public static void pinchIn(WebElement element) {
        AppiumDriver driver = SharedDriver.getDriver();

        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence sequence1 = new Sequence(finger1, 1);
        Sequence sequence2 = new Sequence(finger2, 1);

        // First finger moves from top left to center
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), centerX - 100, centerY - 100));
        sequence1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX, centerY));
        sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Second finger moves from bottom right to center
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), centerX + 100, centerY + 100));
        sequence2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX, centerY));
        sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence1, sequence2));
    }
}