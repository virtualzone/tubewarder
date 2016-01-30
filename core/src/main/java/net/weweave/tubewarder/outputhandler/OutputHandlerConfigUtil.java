package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.Config;
import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

public class OutputHandlerConfigUtil {
    public static Config configJsonStringToMap(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        Config result = new Config();
        for (String key : json.keySet()) {
            result.put(key, json.get(key));
        }
        return result;
    }

    public static String configMapToJsonString(Config config) {
        return configMapToJson(config).toString();
    }

    public static JSONObject configMapToJson(Config config) {
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
