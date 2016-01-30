package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "select";

    private String defaultValue;
    private Map<String, String> options = new LinkedHashMap<>();

    public SelectConfigOption(String id, String label, boolean required, String defaultValue) {
        super(TYPE, id, label, required);
        this.defaultValue = defaultValue;
    }

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> result = super.getAdditionalParameters();
        result.put("options", options);
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
