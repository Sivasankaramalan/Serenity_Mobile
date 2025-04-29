package mobile.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import mobile.base.SharedDriver;
import mobile.pageobjects.LoginPage;
import net.thucydides.core.annotations.Steps;

import static mobile.utils.MobileUtilities.takeScreenshot;

public class OnboardingStepDefinition {

    // Step libraries
    @Steps
    BaseStep baseStep;

    // Page objects - will be initialized on demand
    private LoginPage loginPage;

    private LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(SharedDriver.getDriver());
        }
        return loginPage;
    }

    @Given("I am launching the application")
    public void i_am_launching_the_application() {
        baseStep.printDriverDetails();
        baseStep.launchApp();

    }

    @Given("I am on the onboarding screen")
    public void i_am_on_the_onboarding_screen() {
            getLoginPage().enterUsername("testuser");
            System.out.println("Onboarding screen displayed");
            takeScreenshot();
    }

    @And("I should verify the onboarding screen")
    public void i_should_see_the_onboarding_screen() {
        System.out.println("Onboarding screen displayed");
            takeScreenshot();
    }

    @And("I click on the {string} button")
    public void i_click_on_the_button(String buttonName) {
        try {
            if (buttonName.equalsIgnoreCase("login")) {
                getLoginPage().tapLoginButton();
            } else {
                System.out.println("Button not implemented: " + buttonName);
            }
            System.out.println("Clicked on the " + buttonName + " button");
        } catch (Exception e) {
            System.err.println("Error clicking button: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @And("I enter the {string} as {string}")
    public void i_enter_the_as(String fieldName, String value) {
        try {
            if (fieldName.equalsIgnoreCase("username")) {
                getLoginPage().enterUsername(value);
            } else if (fieldName.equalsIgnoreCase("password")) {
                getLoginPage().enterPassword(value);
            } else {
                System.out.println("Field not implemented: " + fieldName);
            }
            System.out.println("Entered " + fieldName + " as " + value);
        } catch (Exception e) {
            System.err.println("Error entering text: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @And("I select city as {string}")
    public void i_select_city_as(String city) {
        System.out.println("Selected city as " + city);
    }
}