package net.weweave.tubewarder.outputhandler.config;

import java.util.HashMap;
import java.util.Map;

public class SelectConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "select";

    private Map<String, String> options = new HashMap<>();

    public SelectConfigOption(String id, String label, boolean required) {
        super(TYPE, id, label, required);
    }

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> result = super.getAdditionalParameters();
        result.put("options", options);
        return result;
    }
}
