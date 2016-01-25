package net.weweave.tubewarder.outputhandler.config;

import org.json.JSONObject;

public class StringConfigOption extends OutputHandlerConfigOption {
    private String defaultValue;

    public StringConfigOption(String id, String label, boolean required, String defaultValue) {
        super(id, label, required);
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public JSONObject getJson() {
        JSONObject result = super.getJson();
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
