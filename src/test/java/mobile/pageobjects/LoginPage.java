package mobile.pageobjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

import static mobile.utils.MobileUtilities.takeScreenshot;

public class LoginPage {

    public LoginPage(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    // Username Field
    @AndroidFindBy(id = "com.example.android:id/username")
    @iOSXCUITFindBy(id = "username")
    private WebElement usernameField;

    // Password Field
    @AndroidFindBy(id = "com.example.android:id/password")
    @iOSXCUITFindBy(id = "password")
    private WebElement passwordField;

    // Login Button
    @AndroidFindBy(id = "com.example.android:id/loginBtn")
    @iOSXCUITFindBy(id = "loginBtn")
    private WebElement loginButton;


    public void enterUsername(String username) {
            System.out.println("Attempting to enter username: " + username);
            usernameField.clear();
            usernameField.sendKeys(username);
            System.out.println("Username entered successfully");
    }

    public void enterPassword(String password) {
        try {
            System.out.println("Attempting to enter password");
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("Password entered successfully");
        } catch (Exception e) {
            System.err.println("Could not enter password: " + e.getMessage());
            takeScreenshot();
            throw e;
        }
    }

    public void tapLoginButton() {
        try {
            System.out.println("Attempting to tap login button");
            loginButton.click();
            System.out.println("Login button tapped successfully");
        } catch (Exception e) {
            System.err.println("Could not tap login button: " + e.getMessage());
            takeScreenshot();
            throw e;
        }
    }


}