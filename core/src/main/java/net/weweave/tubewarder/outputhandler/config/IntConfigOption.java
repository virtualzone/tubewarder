package net.weweave.tubewarder.outputhandler.config;

import org.json.JSONObject;

public class IntConfigOption extends OutputHandlerConfigOption {
    private int defaultValue;

    public IntConfigOption(String id, String label, boolean required, int defaultValue) {
        super(id, label, required);
        this.defaultValue = defaultValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public JSONObject getJson() {
        JSONObject result = super.getJson();
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
