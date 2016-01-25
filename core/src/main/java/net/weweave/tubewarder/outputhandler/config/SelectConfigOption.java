package net.weweave.tubewarder.outputhandler.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectConfigOption extends OutputHandlerConfigOption {
    private Map<String, String> options = new HashMap<>();

    public SelectConfigOption(String id, String label, boolean required) {
        super(id, label, required);
    }

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    @Override
    public JSONObject getJson() {
        JSONArray options = new JSONArray();
        for (String key : this.options.keySet()) {
            JSONObject o = new JSONObject();
            o.put("key", key);
            o.put("label", this.options.get(key));
            options.put(o);
        }

        JSONObject result = super.getJson();
        result.put("options", options);
        return result;
    }
}
