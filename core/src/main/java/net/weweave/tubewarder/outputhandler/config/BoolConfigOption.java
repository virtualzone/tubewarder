package net.weweave.tubewarder.outputhandler.config;

import org.json.JSONObject;

public class BoolConfigOption extends OutputHandlerConfigOption {
    private boolean defaultValue;

    public BoolConfigOption(String id, String label, boolean required, boolean defaultValue) {
        super(id, label, required);
        this.defaultValue = defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public JSONObject getJson() {
        JSONObject result = super.getJson();
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
