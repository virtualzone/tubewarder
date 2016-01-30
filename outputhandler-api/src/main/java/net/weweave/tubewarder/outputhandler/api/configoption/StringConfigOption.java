package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.Map;

public class StringConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "string";

    private String defaultValue;

    public StringConfigOption(String id, String label, boolean required, String defaultValue) {
        super(TYPE, id, label, required);
        this.defaultValue = defaultValue;
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
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
