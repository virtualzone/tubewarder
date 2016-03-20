package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.Map;

/**
 * A config option for a string.
 */
public class StringConfigOption extends OutputHandlerConfigOption {
    public static final String TYPE = "string";

    private String defaultValue;
    private boolean multiLine;

    public StringConfigOption(String id, String label, boolean required, String defaultValue, boolean multiLine) {
        super(TYPE, id, label, required);
        this.defaultValue = defaultValue;
        this.multiLine = multiLine;
    }

    public StringConfigOption(String id, String label, boolean required, String defaultValue) {
        this(id, label, required, defaultValue, false);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> result = super.getAdditionalParameters();
        result.put("defaultValue", getDefaultValue());
        result.put("multiLine", isMultiLine());
        return result;
    }
}
