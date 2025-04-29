package mobile.pageobjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import java.time.Duration;

public class BasePage extends PageObject {

    // Default timeout for element visibility
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    // Serenity provides enhanced element interactions through WebElementFacade
    protected void waitForVisibility(WebElementFacade element) {
        element.withTimeoutOf(DEFAULT_TIMEOUT).waitUntilVisible();
    }

    protected void clearAndType(WebElementFacade element, String text) {
        element.withTimeoutOf(DEFAULT_TIMEOUT).clear();
        element.type(text);
    }

    protected void clickOn(WebElementFacade element) {
        element.withTimeoutOf(DEFAULT_TIMEOUT).click();
    }

    protected String getAttributeFrom(WebElementFacade element, String attribute) {
        return element.getAttribute(attribute);
    }
}