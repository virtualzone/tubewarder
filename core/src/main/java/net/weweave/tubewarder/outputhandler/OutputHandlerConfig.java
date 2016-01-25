package net.weweave.tubewarder.outputhandler;

import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OutputHandlerConfig {
    public static Map<String, Object> configJsonStringToMap(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        Map<String, Object> result = new HashMap<>();
        for (String key : json.keySet()) {
            result.put(key, json.get(key));
        }
        return result;
    }

    public static String configMapToJsonString(Map<String, Object> config) {
        return configMapToJson(config).toString();
    }

    public static JSONObject configMapToJson(Map<String, Object> config) {
        JSONObject json = new JSONObject();
        for (String key : config.keySet()) {
            json.put(key, config.get(key));
        }
        return json;
    }

    public static boolean isValidConfig(String configJson) {
        if (GenericValidator.isBlankOrNull(configJson)) {
            return false;
        }
        try {
            JSONObject config = new JSONObject(configJson);
            return config.has("id");
        } catch (JSONException e) {
            return false;
        }
    }
}
