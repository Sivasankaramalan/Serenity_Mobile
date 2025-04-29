package mobile.base;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.thucydides.core.util.EnvironmentVariables;

public class BaseTest {

    protected EnvironmentVariables environmentVariables;

    @Before
    public void setUp() {
        // Get environment variables from Serenity
        environmentVariables = ConfiguredEnvironment.getEnvironmentVariables();

        // Log some information about the test setup
        System.out.println("Setting up test with environment: " +
                environmentVariables.getProperty("environment", "default"));
        System.out.println("Platform: " +
                environmentVariables.getProperty("appium.platformName", "unknown"));
    }

    @After
    public void tearDown() {
        System.out.println("Test completed, cleaning up resources");
    }
}