package mobile.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceConfigReader {

    /**
     * Read device configurations from JSON file
     * @param configFile Path to the configuration file
     * @return List of device configuration maps
     */
    public static List<Map<String, String>> readDeviceConfigs(String configFile) {
        List<Map<String, String>> deviceConfigs = new ArrayList<>();

        try (FileReader reader = new FileReader(configFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray devices = jsonObject.getJSONArray("devices");

            for (int i = 0; i < devices.length(); i++) {
                JSONObject device = devices.getJSONObject(i);
                Map<String, String> deviceConfig = new HashMap<>();

                for (String key : device.keySet()) {
                    deviceConfig.put(key, device.getString(key));
                }

                deviceConfigs.add(deviceConfig);
            }
        } catch (IOException e) {
            System.err.println("Error reading device config file: " + e.getMessage());
        }

        return deviceConfigs;
    }
}