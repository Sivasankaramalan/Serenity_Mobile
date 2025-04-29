package mobile.utils;

import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;

import java.util.Properties;

public class SerenityPropertyHelper {

    private SerenityPropertyHelper() {
        // Private constructor to prevent instantiation of this utility class
    }

    public static Properties getProperties() {
        return ConfiguredEnvironment.getEnvironmentVariables().getProperties();
    }

    public static String getProperty(String propertyName) {
        return ConfiguredEnvironment.getEnvironmentVariables().getProperty(propertyName);
    }

    public static String getEnvProperty(String envPropertyName) {
        return EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty(envPropertyName);
    }

    public static String getPropertyWithSerenityDefaults(String propertyKey, String defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null) {
            value = getProperty(propertyKey);
        }
        if (value == null) {
            value = defaultValue;
        }
        if (value != null) {
            System.setProperty(propertyKey, value);
        }
        return value;
    }
}