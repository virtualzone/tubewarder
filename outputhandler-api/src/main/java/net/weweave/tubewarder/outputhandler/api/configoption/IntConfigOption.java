package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.Map;

public class IntConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "int";

    private int defaultValue;

    public IntConfigOption(String id, String label, boolean required, int defaultValue) {
        super(TYPE, id, label, required);
        this.defaultValue = defaultValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> result = super.getAdditionalParameters();
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
