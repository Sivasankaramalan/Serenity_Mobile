package mobile.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.remote.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SessionUtils {

    public static String getSessionId(URL appiumServerUrl) {
        try {
            // Send GET request to /sessions endpoint
            HttpURLConnection connection = (HttpURLConnection) appiumServerUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);  // Timeout for the request
            connection.setReadTimeout(1000);     // Timeout for reading the response

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response to get the session ID
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray sessionArray = jsonResponse.getJSONArray("value");

            if (!sessionArray.isEmpty()) {
                // If there are active sessions, get the first session's ID
                return sessionArray.getJSONObject(0).getString("id");
            } else {
                System.out.println("No active sessions found.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to get existing session ID: " + e.getMessage());
            return null;
        }
    }

    public static <T extends AppiumDriver> void attachToExistingSession(Class<T> driverClass, URL appiumServerUrl, String sessionId) throws MalformedURLException {
        HttpCommandExecutor executor = new HttpCommandExecutor(appiumServerUrl);

        AppiumDriver driver;

        if (driverClass.equals(AndroidDriver.class)) {
            driver = new AndroidDriver(executor, new DesiredCapabilities());
        } else if (driverClass.equals(IOSDriver.class)) {
            driver = new IOSDriver(executor, new DesiredCapabilities());
        } else {
            throw new IllegalArgumentException("Unsupported driver class: " + driverClass.getName());
        }

        // Now forcibly inject the sessionId
        try {
            Field sessionIdField = RemoteWebDriver.class.getDeclaredField("sessionId");
            sessionIdField.setAccessible(true);
            sessionIdField.set(driver, new SessionId(sessionId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to attach to existing session", e);
        }

        System.out.println("Successfully attached to existing session: " + sessionId);
    }



}


