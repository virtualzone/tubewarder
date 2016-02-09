package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.Map;

/**
 * A config option for a boolean (true/false).
 */
public class BoolConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "bool";

    private boolean defaultValue;

    public BoolConfigOption(String id, String label, boolean required, boolean defaultValue) {
        super(TYPE, id, label, required);
        this.defaultValue = defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> result = super.getAdditionalParameters();
        result.put("defaultValue", getDefaultValue());
        return result;
    }
}
